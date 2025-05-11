package com.openketchupsource.soulmate.external.oauth;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
@Getter
public class KakaoProperties {
    private String clientId;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;

    public void setClientId(String clientId) { this.clientId = clientId; }
    public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
    public void setTokenUri(String tokenUri) { this.tokenUri = tokenUri; }
    public void setUserInfoUri(String userInfoUri) { this.userInfoUri = userInfoUri; }
}