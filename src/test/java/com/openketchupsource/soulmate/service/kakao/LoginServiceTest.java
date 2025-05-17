package com.openketchupsource.soulmate.service.kakao;

import com.openketchupsource.soulmate.apiPayload.exception.handler.LoginHandler;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import com.openketchupsource.soulmate.auth.jwt.JwtTokenProvider;
import com.openketchupsource.soulmate.dto.kakao.SocialLoginRequest;
import com.openketchupsource.soulmate.external.oauth.kakao.clientInfo.KakaoAccount;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import com.openketchupsource.soulmate.service.member.MemberService;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

public class LoginServiceTest {

    private KakaoLoginService kakaoLoginService;
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        kakaoLoginService = Mockito.mock(KakaoLoginService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        memberService = Mockito.mock(MemberService.class);

        loginService = new LoginService(kakaoLoginService, jwtTokenProvider, memberService);
    }

    @Test
    void 카카오_사용자정보_누락시_예외_발생() {
        // given
        SocialLoginRequest request = SocialLoginRequest.of("fake_code");

        // 카카오에서 kakaoAccount가 null로 응답되는 경우
        KakaoInfoResponse kakaoInfoResponse = new KakaoInfoResponse(12345L, null);
        Mockito.when(kakaoLoginService.login(any())).thenReturn(kakaoInfoResponse);

        // when & then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(LoginHandler.class)
                .hasMessage(ErrorStatus.KAKAO_INFORM_NOT_EXIST.getMessage());
        
        Throwable thrown = catchThrowable(() -> loginService.login(request));
        System.out.println("Thrown message: " + thrown.getMessage());

    }
    @Test
    void 카카오_프로필_누락시_예외_발생() {
        // given
        SocialLoginRequest request = SocialLoginRequest.of("fake_code");

        // KakaoAccount는 있지만 Profile이 null인 경우
        KakaoAccount account = new KakaoAccount("test@example.com", null);
        KakaoInfoResponse kakaoInfoResponse = new KakaoInfoResponse(12345L, account);
        Mockito.when(kakaoLoginService.login(any())).thenReturn(kakaoInfoResponse);

        // when & then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(LoginHandler.class)
                .hasMessage(ErrorStatus.KAKAO_INFORM_NOT_EXIST.getMessage());
        Throwable thrown = catchThrowable(() -> loginService.login(request));
        System.out.println("Thrown message: " + thrown.getMessage());
    }
}
