package com.openketchupsource.soulmate.service.diary;

import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    private void setDate(Diary diary, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        diary.setDate(date);
    }
}
