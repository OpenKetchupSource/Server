package com.openketchupsource.soulmate.controller.login;

import com.openketchupsource.soulmate.service.kakao.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 디버깅용 테스트 컨트롤러
 * 카카오 인가코드 직접 받을 수 있음
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class AuthCheckController {

    private final KakaoLoginService kakaoLoginService;
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
