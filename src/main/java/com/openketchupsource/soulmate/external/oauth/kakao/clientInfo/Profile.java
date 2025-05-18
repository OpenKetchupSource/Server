package com.openketchupsource.soulmate.external.oauth.kakao.clientInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * KakaoAccount의 profile 필드에서 닉네임, 프로필 이미지를 파싱
 * @param nickname
 * @param profileImageUrl
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Profile(
        String nickname,
        String profileImageUrl
) {}