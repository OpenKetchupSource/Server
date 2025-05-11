package com.openketchupsource.soulmate.external.oauth.kakao.request;

import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoInfoClient", url = "https://kapi.kakao.com/v2/user/me")
public interface KakaoInfoClient {

    @GetMapping
    KakaoInfoResponse kakaoInfo(
            @RequestHeader("Authorization") String token,
            @RequestHeader(name = "Content-type") String contentType
    );
}
