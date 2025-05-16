package com.openketchupsource.soulmate.service.member;

import com.openketchupsource.soulmate.auth.jwt.JwtTokenProvider;
import com.openketchupsource.soulmate.auth.jwt.TokenResponse;
import com.openketchupsource.soulmate.external.oauth.dto.SocialLoginRequest;
import com.openketchupsource.soulmate.external.oauth.dto.SocialLoginResponse;
import com.openketchupsource.soulmate.external.oauth.kakao.KakaoSocialLoginService;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoAccount;
import com.openketchupsource.soulmate.external.oauth.kakao.response.KakaoInfoResponse;
import com.openketchupsource.soulmate.external.oauth.kakao.response.Profile;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.openketchupsource.soulmate.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    private final KakaoSocialLoginService kakaoSocialLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public SocialLoginResponse login(SocialLoginRequest socialLoginRequest) {
        Member member = null;
        KakaoInfoResponse kakaoInfoResponse = kakaoSocialLoginService.login(socialLoginRequest);
        String sub = String.valueOf(kakaoInfoResponse.id());  // Long → String

        if (memberService.isExistsBySub(sub)) {
            member = memberService.findBySub(sub);
        } else {
            KakaoAccount account = kakaoInfoResponse.kakaoAccount();
            if (account == null || account.profile() == null) {
                throw new IllegalStateException("카카오 사용자 정보가 누락되었습니다.");
            }else {
                String name = Optional.ofNullable(account)
                        .map(KakaoAccount::profile)
                        .map(Profile::nickname)
                        .orElse("unknown");

                String email = Optional.ofNullable(account)
                        .map(KakaoAccount::email)
                        .orElse("unknown@example.com");
                member = Member.builder()
                        .name(name)
                        .email(email != null ? email : "no-email@kakao.com")  // 이메일 동의 안 했을 경우 대비
                        .sub(sub)
                        .build();
                memberService.saveMember(member);
            }

        }
        try {
            return SocialLoginResponse.of(signUp(member.getId()));
        } catch (NullPointerException e) {
            throw new EntityNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    public TokenResponse signUp(Long memberId) {
        return TokenResponse.of(jwtTokenProvider.generateAccessToken(memberId), jwtTokenProvider.generateRefreshToken(memberId));
    }
}
