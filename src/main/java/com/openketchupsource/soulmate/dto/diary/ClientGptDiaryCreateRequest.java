package com.openketchupsource.soulmate.dto.diary;

import java.time.LocalDate;

// 프론트에서 우리한테 AI 일기 생성 요청 보낼 때
public record ClientGptDiaryCreateRequest(
    Long chatId,
    String character,
    LocalDate date
) {}
