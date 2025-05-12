package com.openketchupsource.soulmate.member.auth;

import com.openketchupsource.soulmate.member.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MemberDetails implements UserDetails {

    private final MemberEntity member;

    public MemberDetails(MemberEntity member) {
        this.member = member;
    }

    public MemberEntity getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한이 있다면 여기에 추가
    }

    @Override
    public String getPassword() {
        return ""; // 소셜 로그인이니까 비워두기
    }

    @Override
    public String getUsername() {
        return member.getName(); // 사용자 이름 불러오기
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}