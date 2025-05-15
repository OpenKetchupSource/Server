package com.openketchupsource.soulmate.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.openketchupsource.soulmate.member.entity.Member;
import com.openketchupsource.soulmate.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(final Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 회원이 존재하지 않습니다.")
        );
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
