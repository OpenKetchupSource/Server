package com.openketchupsource.soulmate.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities); // principal: 사용자 정보, credentials: 비밀번호(JWT 기반이니까 null), authorities : 권한 목록 (null)
    }

    public static MemberAuthentication createMemberAuthentication(Long memberId) {
        return new MemberAuthentication(memberId, null, null);
    }
}
