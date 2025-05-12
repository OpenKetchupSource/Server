package com.openketchupsource.soulmate.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatReply2ClientDto {
    private String role;
    private String content;
}
