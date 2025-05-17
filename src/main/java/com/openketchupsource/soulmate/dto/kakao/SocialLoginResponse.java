package com.openketchupsource.soulmate.dto.kakao;

import com.openketchupsource.soulmate.dto.TokenResponse;

/**
 * 로그인 성공 시 백엔드가 프론트에게 주는 응답
 * TokenResponse에는 access_token과 refresh_token이 저장됨
 * @param tokens
 */
public record SocialLoginResponse(
        TokenResponse tokens
) {
    public static SocialLoginResponse of(TokenResponse tokens) {
        return new SocialLoginResponse(tokens);
    }
}