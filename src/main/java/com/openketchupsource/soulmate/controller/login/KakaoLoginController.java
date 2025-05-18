package com.openketchupsource.soulmate.controller.login;

import com.openketchupsource.soulmate.apiPayload.ApiResponse;
import com.openketchupsource.soulmate.dto.kakao.SocialLoginRequest;
import com.openketchupsource.soulmate.dto.kakao.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import com.openketchupsource.soulmate.service.kakao.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 실제 사용될 카카오 로그인 시 사용될 컨트롤러
 * 프론트로부터 받은 인가 코드를 SocialLoginRequest DTO로 변환하여 login 함수의 파라미터로 사용
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/login")
public class KakaoLoginController {
    private final LoginService loginService;
    @PostMapping
    public ResponseEntity<ApiResponse<SocialLoginResponse>> login(@RequestBody SocialLoginRequest socialLoginRequest) {
        return ResponseEntity.ok(ApiResponse.onSuccess(loginService.login(socialLoginRequest)));
    }
}
