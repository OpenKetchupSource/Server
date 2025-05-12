package com.openketchupsource.soulmate.controller.chat;

import com.openketchupsource.soulmate.dto.chat.ChatMessageDto;
import com.openketchupsource.soulmate.dto.chat.ChatReply2ClientDto;
import com.openketchupsource.soulmate.service.chat.ChatAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatAIService chatAIService;

    public ChatController(ChatAIService openAiService) {
        this.chatAIService = openAiService;
    }

    @PostMapping("/reply")
    public ChatReply2ClientDto reply(@RequestBody List<ChatMessageDto> messages,
                                     @RequestParam String character) throws Exception {
        return chatAIService.getReply(character, messages);
    }

    @PostMapping("/diary")
    public ResponseEntity<ChatReply2ClientDto> diary(@RequestParam String character,
                                                     @RequestBody List<ChatMessageDto> messages) throws Exception {
        ChatReply2ClientDto diary = chatAIService.getDiary(character, messages);
        return ResponseEntity.ok(diary);
    }
}

