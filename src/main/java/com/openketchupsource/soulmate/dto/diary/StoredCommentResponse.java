package com.openketchupsource.soulmate.dto.diary;

public record StoredCommentResponse(
        Long id,
        String context,
        int isStored,
        String character
) {
    public static StoredCommentResponse of(Long id, String context, int isStored, String character) {
        return new StoredCommentResponse(id, context, isStored, character);
    }
}
