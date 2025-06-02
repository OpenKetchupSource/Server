package com.openketchupsource.soulmate.dto.diary;

import lombok.Getter;

public class CommentRequest {
    @Getter
    public static class AddDto{
        String context;
        Long characterId;
    }
}
