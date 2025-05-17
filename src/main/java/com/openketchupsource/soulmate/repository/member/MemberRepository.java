package com.openketchupsource.soulmate.repository.member;

import com.openketchupsource.soulmate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsBySub(String sub);
    Optional<Member> findBySub(String sub);
}