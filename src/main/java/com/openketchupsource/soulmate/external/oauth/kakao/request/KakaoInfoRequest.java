package com.openketchupsource.soulmate.external.oauth.kakao.request;

import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 발급받은 access token으로 카카오 사용자 정보를 요청하는 역할
 */
@FeignClient(value = "kakaoInfoClient", url = "https://kapi.kakao.com/v2/user/me")
public interface KakaoInfoRequest {

    @GetMapping
    KakaoInfoResponse kakaoInfo(
            @RequestHeader("Authorization") String token,
            @RequestHeader(name = "Content-type") String contentType
    );
}
