package com.openketchupsource.soulmate.controller.login;

import com.openketchupsource.soulmate.external.oauth.dto.SocialLoginRequest;
import com.openketchupsource.soulmate.external.oauth.dto.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import com.openketchupsource.soulmate.service.member.SocialLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/login")
public class SocialLoginController {
    private final SocialLoginService socialLoginUseCase;
    @PostMapping
    public ResponseEntity<SocialLoginResponse> login(@RequestBody SocialLoginRequest socialLoginRequest) {
        return ResponseEntity.ok(socialLoginUseCase.login(socialLoginRequest));
    }
}
