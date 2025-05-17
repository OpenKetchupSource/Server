package com.openketchupsource.soulmate.service.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openketchupsource.soulmate.apiPayload.exception.handler.LoginHandler;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import com.openketchupsource.soulmate.dto.kakao.SocialLoginRequest;
import com.openketchupsource.soulmate.external.oauth.kakao.request.KakaoTokenRequest;
import com.openketchupsource.soulmate.external.oauth.kakao.request.KakaoInfoRequest;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoTokenResponse;
import com.openketchupsource.soulmate.external.oauth.KakaoProperties;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 인가코드를 이용하여 kakao에 액세스 토큰 및 사용자 정보 요청
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoInfoRequest kakaoInfoRequest;
    private final KakaoTokenRequest kakaoTokenRequest;
    private final KakaoProperties kakaoProperties;

    public KakaoInfoResponse login(SocialLoginRequest socialLoginRequest) {
        // 1. kakao에 정보 요청을 위해 필요한 데이터 및 인가코드를 담은 body 생성
        Map<String, String> body = Map.of(
                "grant_type", "authorization_code",
                "client_id", kakaoProperties.getClientId(),
                "redirect_uri", kakaoProperties.getRedirectUri(),
                "code", socialLoginRequest.code()
        );

        try {
            // 2. body를 실제로 kakao에 보낸 뒤 받은 json 저장
            KakaoTokenResponse tokenResponse = kakaoTokenRequest.getToken(
                    "application/x-www-form-urlencoded",
                    body
            );

            // 3. json 응답에서 accessToken만 추출
            String accessToken = tokenResponse.accessToken();

            // 4. accessToken으로 사용자 정보 요청
            return kakaoInfoRequest.kakaoInfo(
                    "Bearer " + accessToken,
                    "application/x-www-form-urlencoded;charset=utf-8"
            );
        } catch (FeignException.BadRequest e) {
            // 1. 에러 body 가져오기 (JSON 형식)
            String responseBody = e.contentUTF8();

            // 2. JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsed;
            try {
                parsed = mapper.readValue(responseBody, Map.class);
            } catch (Exception ex) {
                throw new LoginHandler(ErrorStatus.KAKAO_INVALID_CODE, "Kakao 응답 파싱 실패");
            }

            // 3. result에 넣을 Map 형태로 커스텀 예외 던지기
            throw new LoginHandler(ErrorStatus.KAKAO_INVALID_CODE, parsed);
        }

    }

}
