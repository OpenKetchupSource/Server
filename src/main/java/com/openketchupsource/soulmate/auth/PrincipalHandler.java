package com.openketchupsource.soulmate.auth;

import org.springframework.security.core.context.SecurityContextHolder;

// SecurityContext에 저장된 로그인 사용자 정보에서 memberId를 꺼내서 Long 타입으로 리턴해주는 정적 헬퍼 메서드
public class PrincipalHandler {
    public static Long getMemberIdFromPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(principal.toString());
    }
}
