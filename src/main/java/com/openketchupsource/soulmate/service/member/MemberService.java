package com.openketchupsource.soulmate.service.member;

import com.openketchupsource.soulmate.apiPayload.exception.handler.DiaryHandler;
import com.openketchupsource.soulmate.apiPayload.exception.handler.SettingHandler;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.repository.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new DiaryHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public boolean isExistsBySub(String sub) {
        return memberRepository.existsBySub(sub);
    }

    public Member findBySub(String sub) {
        return memberRepository.findBySub(sub).orElseThrow(
                () -> new EntityNotFoundException("해당 소셜로 회원가입한 회원이 존재하지 않습니다.")
        );
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
