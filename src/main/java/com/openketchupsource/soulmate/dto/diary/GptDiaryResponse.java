package com.openketchupsource.soulmate.dto.diary;

// 지피티한테 받은 응답
public record GptDiaryResponse(
        String title,
        String content,
        String hashtag,
        String character // "웅이", "티바노", "앙글이"
) {}