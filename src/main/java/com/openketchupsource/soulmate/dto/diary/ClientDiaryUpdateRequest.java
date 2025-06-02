package com.openketchupsource.soulmate.dto.diary;

import java.time.LocalDate;

public record ClientDiaryUpdateRequest(
        LocalDate date,
        String title,
        String content,
        String hashtag
) {
}
