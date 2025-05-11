package com.openketchupsource.soulmate.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialPlatform {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO");
    private final String socialPlatform;
}
