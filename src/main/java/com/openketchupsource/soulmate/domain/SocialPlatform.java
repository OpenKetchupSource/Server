package com.openketchupsource.soulmate.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialPlatform {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO");
    private final String socialPlatform;
}
