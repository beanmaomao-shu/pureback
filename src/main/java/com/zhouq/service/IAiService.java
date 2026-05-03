package com.zhouq.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IAiService {
    Map<String, Object> recognize(MultipartFile file);
}