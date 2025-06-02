package com.openketchupsource.soulmate.dto.diary;

import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.HashTag;

import java.time.LocalDate;
import java.util.List;

public record ClientDiaryResponse(
        Long id,
        LocalDate date,
        String title,
        String content,
        String hashtag,
        String character
) {}

