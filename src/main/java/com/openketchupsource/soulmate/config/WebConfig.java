package com.openketchupsource.soulmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 경로에 대해
                .allowedOrigins("http://localhost:3000", "http://localhost:5713", "https://soulmate.o-r.kr") // 이 두 프론트엔드 도메인에서 오는 요청 허용
                .allowedMethods("*") // GET, POST, PUT, DELETE 등 모든 HTTP 메서드 허용
                .allowCredentials(true); // 인증 정보(쿠키 등) 포함 가능
    }
}
