package com.openketchupsource.soulmate.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// OpenAI API의 응답을 역직렬화
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseDto {
    private List<Choice> choices;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private String content;
    }
}
