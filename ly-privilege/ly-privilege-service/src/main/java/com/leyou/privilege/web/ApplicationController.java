package com.leyou.privilege.web;

import com.leyou.privilege.dto.ApplicationDTO;
import com.leyou.privilege.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 9:38
 */
@RestController
@RequestMapping("app")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /**
     * 新增服务信息
     * @param applicationDTO 应用信息
     * @return 无
     */
    @PostMapping
    public ResponseEntity<Void> saveApplication(ApplicationDTO applicationDTO) {
        applicationService.save(applicationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
