package com.openketchupsource.soulmate.controller.chat;

import com.openketchupsource.soulmate.dto.chat.ChatInitResponseDto;
import com.openketchupsource.soulmate.dto.chat.ChatMessageDto;
import com.openketchupsource.soulmate.dto.chat.ChatReply2ClientDto;
import com.openketchupsource.soulmate.service.chat.ChatAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* 아직 사용자, 일기랑 연결 안 함 -> 그런데 굳이? 싶긴함 ㅇㅇ */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatAIService chatAIService;

    // 대화 시작 - chatId 생성
    @PostMapping("/start")
    public ResponseEntity<ChatInitResponseDto> startChat(@RequestParam String character) {
        ChatInitResponseDto initMessage = chatAIService.createChat(character);
        return ResponseEntity.ok(initMessage);
    }

    // 메시지 추가 + 응답
    @PostMapping("/{chatId}/reply")
    public ResponseEntity<ChatReply2ClientDto> reply(
            @PathVariable Long chatId,
            @RequestBody ChatMessageDto message) throws Exception {

        chatAIService.appendMessage(chatId, message); // DB에 메시지 누적
        ChatReply2ClientDto reply = chatAIService.getReply(chatId); // 누적된 메시지 기반 응답 생성

        return ResponseEntity.ok(reply);
    }

    // 전체 대화 조회
    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long chatId) {
        List<ChatMessageDto> history = chatAIService.getChatMessages(chatId);
        return ResponseEntity.ok(history);
    }
}

