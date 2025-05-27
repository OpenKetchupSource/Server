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
    public static DiaryListResponse of(Long id, LocalDate date, String title, String content, List<HashTag> hashTags) {
        return new DiaryListResponse(id, date, title, content, hashTags);
    }
}
