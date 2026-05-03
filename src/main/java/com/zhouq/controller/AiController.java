package com.zhouq.controller;

import com.zhouq.common.result.Result;
import com.zhouq.service.IAiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final IAiService aiService;

    public AiController(IAiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result recognize(@RequestPart("file") MultipartFile file) {
        return Result.success("识别成功", aiService.recognize(file));
    }
}