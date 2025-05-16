package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
