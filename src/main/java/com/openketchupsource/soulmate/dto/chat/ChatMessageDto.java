package com.openketchupsource.soulmate.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String role; // "system", "user", "assistant"
    private String content;

    public ChatMessageDto(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // getter, setter
}

