package com.openketchupsource.soulmate.external.oauth.kakao;

import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "kakaoTokenClient", url = "https://kauth.kakao.com")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoTokenResponse getToken(@RequestHeader("Content-Type") String contentType,
                                @RequestBody Map<String, ?> body);
}