package com.openketchupsource.soulmate.controller.login;

import com.openketchupsource.soulmate.external.oauth.kakao.KakaoSocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final KakaoSocialLoginService kakaoSocialLoginService;
    // 인가 코드 받기 테스트
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) {
        // 임시 확인용 메시지
        return ResponseEntity.ok("인가 코드: " + code);

        // 추후에는 여기에 access token 요청 및 회원가입 로직 연결
        // SocialLoginRequest request = new SocialLoginRequest(code);
        // return ResponseEntity.ok(kakaoSocialLoginService.login(request));
    }
}
