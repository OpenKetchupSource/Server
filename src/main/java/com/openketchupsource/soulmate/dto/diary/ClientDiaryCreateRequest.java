package com.openketchupsource.soulmate.dto.diary;

import java.time.LocalDate;

public record ClientDiaryCreateRequest(
        LocalDate date,
        String title,
        String content,
        String hashtag,
        String character
) {
}
