package com.openketchupsource.soulmate.dto.diary;

import java.time.LocalDate;

public record CommentListResponse(
        Long id,
        String context,
        int isStored,
        String character,
        LocalDate date
) {
    public static CommentListResponse of(Long id, String context, int isStored, String character, LocalDate date) {
        return new CommentListResponse(id, context, isStored, character, date);
    }
}
