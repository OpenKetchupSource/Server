package com.openketchupsource.soulmate.external.oauth.kakao.request;

import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * 카카오에 실제로 인가 코드를 보내 access token을 발급받는 역할
 */
@FeignClient(name = "kakaoTokenClient", url = "https://kauth.kakao.com")
public interface KakaoTokenRequest {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoTokenResponse getToken(@RequestHeader("Content-Type") String contentType,
                                @RequestBody Map<String, ?> body);
}