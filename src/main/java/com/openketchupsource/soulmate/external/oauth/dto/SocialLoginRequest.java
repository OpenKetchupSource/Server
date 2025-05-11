package com.openketchupsource.soulmate.external.oauth.dto;

public record SocialLoginRequest(
        String code
) {
    public static SocialLoginRequest of(String code) {
        return new SocialLoginRequest(code);
    }
}