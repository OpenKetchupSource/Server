package com.openketchupsource.soulmate.external.oauth.dto;

import com.openketchupsource.soulmate.auth.jwt.TokenResponse;

public record SocialLoginResponse(
        TokenResponse tokens
) {
    public static SocialLoginResponse of(TokenResponse tokens) {
        return new SocialLoginResponse(tokens);
    }
}
