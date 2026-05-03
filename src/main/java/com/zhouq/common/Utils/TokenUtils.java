package com.zhouq.common.Utils;

import lombok.Data;
import org.apache.poi.ss.usermodel.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.xml.datatype.Duration;
import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/7/9 15:45
 */
@Component
public class TokenUtils {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String ACCESS_TOKEN = "token:access_token:";
    private final String FRESH_TOKEN = "token:fresh_token:";
    private final long FRESH_TIMEOUT = 24L;
    private final long ACCESS_TIMEOUT = 1L;
    private final TimeUnit TIME_UNIT = TimeUnit.DAYS;
    private final String VALUE = "0";

    private String generatorToken(String type) {
        return type + UUID.randomUUID();
    }

    public String generatorFreshToken() {
        String token = generatorToken(FRESH_TOKEN);
        saveToken(token, FRESH_TIMEOUT, TIME_UNIT);
        return token;
    }

    public String generatorAccessToken() {
        String token = generatorToken(ACCESS_TOKEN);
        saveToken(token, ACCESS_TIMEOUT, TIME_UNIT);
        return token;
    }

    public boolean checkToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public void saveToken(String token, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(token, VALUE, timeout, timeUnit);
    }

    public String freshToken(String freshToken) {
        if (checkToken(freshToken)) {
            saveToken(freshToken, FRESH_TIMEOUT, TIME_UNIT);
            return generatorToken(ACCESS_TOKEN);
        } else {
            return "";
        }
    }

    public void delete(String token) {
        if (checkToken(token)) {
            redisTemplate.delete(token);
        }
    }

    public LocalDateTime getExpires(String key) {
        Long expire = redisTemplate.getExpire(key);
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(expire);
        return localDateTime;
    }
}
