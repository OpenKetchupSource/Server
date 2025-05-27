package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMember(Member member);
}
