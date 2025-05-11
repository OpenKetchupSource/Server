package com.openketchupsource.soulmate.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 배포 확인용...
@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/test")
    public String test() {
        return "정상 작동 중입니다";
    }
}
