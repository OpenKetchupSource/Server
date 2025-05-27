package com.openketchupsource.soulmate.dto.diary;

import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.domain.HashTag;

import java.time.LocalDate;
import java.util.List;

public record DiaryResponse(
        Long id,
        LocalDate date,
        String title,
        String content,
        Comment comment,
        Character character,
        List<HashTag> hashTags
) {
}
