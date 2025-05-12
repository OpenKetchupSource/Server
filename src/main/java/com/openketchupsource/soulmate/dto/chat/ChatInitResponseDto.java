package com.openketchupsource.soulmate.dto.chat;
import lombok.Getter;

@Getter
public class ChatInitResponseDto {
    private Long chatId;
    private String role;
    private String content;

    public ChatInitResponseDto(Long chatId, String role, String content) {
        this.chatId = chatId;
        this.role = role;
        this.content = content;
    }
}
