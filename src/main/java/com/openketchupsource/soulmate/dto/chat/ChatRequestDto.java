package com.openketchupsource.soulmate.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRequestDto {
    private String model;
    private List<ChatMessageDto> messages;

    public ChatRequestDto(String model, List<ChatMessageDto> messages) {
        this.model = model;
        this.messages = messages;
    }

    // getter, setter
}

