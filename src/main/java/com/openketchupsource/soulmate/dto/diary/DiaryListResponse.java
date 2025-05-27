package com.openketchupsource.soulmate.dto.diary;

import com.openketchupsource.soulmate.domain.HashTag;

import java.time.LocalDate;
import java.util.List;

public record DiaryListResponse(
        Long id,
        LocalDate date,
        String title,
        String content,
        List<HashTag> hashTags
) {
}
