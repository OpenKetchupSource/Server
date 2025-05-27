package com.openketchupsource.soulmate.dto.diary;


import java.time.LocalDate;
import java.util.List;

public record DiaryListResponse(
        Long id,
        LocalDate date,
        String title,
        String content,
        List<String> hashTags
) {
    public static DiaryListResponse of(Long id, LocalDate date, String title, String content, List<String> hashTags) {
        return new DiaryListResponse(id, date, title, content, hashTags);
    }
}
