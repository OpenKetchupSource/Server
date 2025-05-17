package com.openketchupsource.soulmate.external.oauth.kakao.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 카카오로부터 받은 토큰 정보를 저장하는 객체
 * @param accessToken
 * @param refreshToken
 * @param tokenType
 * @param expiresIn
 */
public record KakaoTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn
) {}