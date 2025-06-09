package com.openketchupsource.soulmate.dto.diary;

import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Comment;

import java.time.LocalDate;
import java.util.List;

public record DiaryResponse(
        Long id,
        LocalDate date,
        String title,
        String content,
        Long commentId,
        String comment,
        int isStored,
        String character,
        List<String> hashTags
) {
    public static DiaryResponse of(Long id, LocalDate date, String title, String content, Long commentId, String comment, int isStored, String character, List<String> hashTags) {
        return new DiaryResponse(id, date, title, content, commentId, comment, isStored, character, hashTags);
    }
}
