package com.openketchupsource.soulmate.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String[] AUTH_WHITE_LIST = {  //인증 없이 접근 가능한 경로 목록
            "/h2-console/**", "/oauth/**", "/api/oauth/**"
            //"/oauth/**",  // 인가코드 콜백을 받기 위함
            //"/api/oauth/**" // POST 요청도 가능하도록
    };  //인증 없이 접근 가능한 경로 목록

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)               // REST API에서 보통 꺼둠
                .formLogin(AbstractHttpConfigurer::disable)      // HTML form 기반 로그인 비활성화 (우린 JWT 기반이라 필요 없음)
                .requestCache(RequestCacheConfigurer::disable)   // 인증 실패 후 이전 페이지로 리디렉션하는 기능 비활성화
                .httpBasic(AbstractHttpConfigurer::disable);     // 브라우저 기본 인증창 방식 비활성화

        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AUTH_WHITE_LIST).permitAll();  // AUTH_WHITE_LIST에 해당하는 경로만 비인증 허용
                    auth.anyRequest().authenticated();    // 나머지 모든 요청은 인증 필요
                })
                // Spring Security 기본 필터 체인에서 UsernamePasswordAuthenticationFilter보다 앞에 JWT 필터를 끼워 넣음 => 그래야 JWT로 인증된 사용자 정보가 먼저 등록되고, 이후의 인증 처리에도 반영됨
                .headers(headers -> headers.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}