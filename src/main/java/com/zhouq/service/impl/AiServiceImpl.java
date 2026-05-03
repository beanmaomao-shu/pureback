package com.zhouq.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouq.service.IAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.Base64;

@Service
public class AiServiceImpl implements IAiService {

    @Value("${ai.openai.base-url}")
    private String baseUrl;

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model}")
    private String model;

    @Value("${ai.openai.prompt}")
    private String prompt;

    @Value("${ai.openai.connect-timeout-ms:10000}")
    private int connectTimeoutMs;

    @Value("${ai.openai.read-timeout-ms:60000}")
    private int readTimeoutMs;

    private final ObjectMapper objectMapper;

    public AiServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> recognize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件为空");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("仅支持图片文件");
        }
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("未配置AI_API_KEY");
        }

        try {
            String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
            String imageUrl = "data:" + file.getContentType() + ";base64," + imageBase64;

            Map<String, Object> textPart = new LinkedHashMap<>();
            textPart.put("type", "text");
            textPart.put("text", prompt);

            Map<String, Object> imageUrlObj = new LinkedHashMap<>();
            imageUrlObj.put("url", imageUrl);

            Map<String, Object> imagePart = new LinkedHashMap<>();
            imagePart.put("type", "image_url");
            imagePart.put("image_url", imageUrlObj);

            List<Object> content = new ArrayList<>();
            content.add(textPart);
            content.add(imagePart);

            Map<String, Object> message = new LinkedHashMap<>();
            message.put("role", "user");
            message.put("content", content);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", model);
            body.put("messages", Collections.singletonList(message));
            body.put("temperature", 0.2);
            body.put("max_tokens", 500);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = buildRestTemplate().exchange(
                buildChatCompletionsUrl(),
                HttpMethod.POST,
                requestEntity,
                Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("AI服务返回为空");
            }

            String contentText = extractContentText(responseBody);
            String jsonText = extractJson(contentText);

            Map<String, Object> parsed = objectMapper.readValue(
                jsonText,
                new TypeReference<Map<String, Object>>() {}
            );

            String chineseName = Objects.toString(parsed.get("chineseName"), "");
            String latinName = Objects.toString(parsed.get("latinName"), "");
            String description = Objects.toString(parsed.get("description"), "");
            double confidence = parseConfidence(parsed.get("confidence"));

            if (confidence < 0) confidence = 0;
            if (confidence > 100) confidence = 100;

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("chineseName", chineseName);
            result.put("latinName", latinName);
            result.put("description", description);
            result.put("confidence", String.format(Locale.US, "%.2f", confidence));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("AI识别失败: " + e.getMessage());
        }
    }

    private RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMs);
        factory.setReadTimeout(readTimeoutMs);
        return new RestTemplate(factory);
    }

    private String buildChatCompletionsUrl() {
        if (baseUrl.endsWith("/")) {
            return baseUrl + "chat/completions";
        }
        return baseUrl + "/chat/completions";
    }

    private String extractContentText(Map<String, Object> responseBody) {
        Object choicesObj = responseBody.get("choices");
        if (!(choicesObj instanceof List) || ((List<?>) choicesObj).isEmpty()) {
            throw new RuntimeException("AI返回格式异常: choices为空");
        }

        Object firstChoice = ((List<?>) choicesObj).get(0);
        if (!(firstChoice instanceof Map)) {
            throw new RuntimeException("AI返回格式异常: choice结构错误");
        }

        Object messageObj = ((Map<?, ?>) firstChoice).get("message");
        if (!(messageObj instanceof Map)) {
            throw new RuntimeException("AI返回格式异常: message结构错误");
        }

        Object contentObj = ((Map<?, ?>) messageObj).get("content");
        if (contentObj instanceof String) {
            return (String) contentObj;
        }

        if (contentObj instanceof List) {
            StringBuilder sb = new StringBuilder();
            for (Object item : (List<?>) contentObj) {
                if (item instanceof Map) {
                    Object text = ((Map<?, ?>) item).get("text");
                    if (text != null) sb.append(text.toString());
                }
            }
            if (sb.length() > 0) return sb.toString();
        }

        throw new RuntimeException("AI返回格式异常: content解析失败");
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start < 0 || end < 0 || end <= start) {
            throw new RuntimeException("AI未返回JSON结构");
        }
        return text.substring(start, end + 1);
    }

    private double parseConfidence(Object confidence) {
        if (confidence == null) return 0;
        String v = confidence.toString().replace("%", "").trim();
        try {
            return Double.parseDouble(v);
        } catch (Exception e) {
            return 0;
        }
    }
}