package com.openketchupsource.soulmate.dto.diary;

import java.util.List;

public record GptDiaryPrompt(
        String character,   // 앙글이, 웅이, 티바노
        List<ChatLine> messages
) {
    public record ChatLine(String role, String content) {}
}