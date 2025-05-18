package com.openketchupsource.soulmate.dto.kakao;

/**
 * 인가코드를 받는 DTO
 * 프론트 -> 백엔드 요청 시 사용
 * @param code
 */
public record SocialLoginRequest(
        String code
) {
    public static SocialLoginRequest of(String code) {
        return new SocialLoginRequest(code);
    }
}