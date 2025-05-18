package com.openketchupsource.soulmate.controller.login;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * 배포 확인용 컨트롤러
 */
@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "정상 작동 중입니다");
        return result;
    }
}
