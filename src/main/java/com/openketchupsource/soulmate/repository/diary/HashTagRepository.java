package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByName(String name);

    @Query("SELECT DISTINCT h FROM HashTag h JOIN h.diaries d WHERE d.member.id = :memberId")
    List<HashTag> findAllByMemberId(Long memberId);
}
