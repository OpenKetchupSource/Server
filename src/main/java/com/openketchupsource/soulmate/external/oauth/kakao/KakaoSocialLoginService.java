package com.openketchupsource.soulmate.external.oauth.kakao;

import com.openketchupsource.soulmate.external.oauth.dto.SocialLoginRequest;
import com.openketchupsource.soulmate.external.oauth.kakao.request.KakaoInfoClient;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoTokenResponse;
import com.openketchupsource.soulmate.external.oauth.KakaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSocialLoginService {

    private final KakaoInfoClient kakaoInfoClient;
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoProperties kakaoProperties;

    public KakaoInfoResponse login(SocialLoginRequest socialLoginRequest) {
        // 1. 인가코드로 액세스 토큰 요청
        Map<String, String> body = Map.of(
                "grant_type", "authorization_code",
                "client_id", kakaoProperties.getClientId(),
                "redirect_uri", kakaoProperties.getRedirectUri(),
                "code", socialLoginRequest.code()
        );

        KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(
                "application/x-www-form-urlencoded",
                body
        );

        String accessToken = tokenResponse.accessToken();

        // 2. 사용자 정보 요청
        return kakaoInfoClient.kakaoInfo(
                "Bearer " + accessToken,
                "application/x-www-form-urlencoded;charset=utf-8"
        );
    }

}
