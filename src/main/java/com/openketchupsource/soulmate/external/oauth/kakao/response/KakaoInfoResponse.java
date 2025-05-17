package com.openketchupsource.soulmate.external.oauth.kakao.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.openketchupsource.soulmate.external.oauth.kakao.clientInfo.KakaoAccount;

/**
 * 카카오 사용자 ID + 계정 정보(email 등)를 담은 응답 객체
 * @param id
 * @param kakaoAccount
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoInfoResponse(
        Long id,
        KakaoAccount kakaoAccount
) {}
