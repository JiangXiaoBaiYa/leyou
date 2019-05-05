package com.leyou.upload.controller;

import com.leyou.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 9:53
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片功能
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        //返回200，并且携带url路径
        return ResponseEntity.ok(uploadService.upload(file));
    }

    /**
     * 上传至阿里云OSS
     */
    @GetMapping("signature")
    public ResponseEntity<Map<String, Object>> getAliSignature() {
        return ResponseEntity.ok(uploadService.getSignature());
    }
}
