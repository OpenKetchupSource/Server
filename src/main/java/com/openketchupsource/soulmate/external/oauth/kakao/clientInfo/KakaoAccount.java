package com.openketchupsource.soulmate.external.oauth.kakao.clientInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * KakaoInfoResponse에서 받은 kakao_account 필드를 파싱
 * @param email
 * @param profile
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(
        String email,
        Profile profile
) {}
