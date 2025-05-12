package com.openketchupsource.soulmate.controller.chat;

import com.openketchupsource.soulmate.dto.chat.ChatMessageDto;
import com.openketchupsource.soulmate.dto.chat.ChatReply2ClientDto;
import com.openketchupsource.soulmate.member.auth.MemberDetails;
import com.openketchupsource.soulmate.member.entity.MemberEntity;
import com.openketchupsource.soulmate.member.service.MemberService;
import com.openketchupsource.soulmate.service.chat.ChatAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* 아직 사용자, 일기랑 연결 안 함 -> 그런데 굳이? 싶긴함 ㅇㅇ */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatAIService chatAIService;
    // private final MemberService memberService;

    // 1. 대화 시작 - chatId 생성
    @PostMapping("/start")
    public ResponseEntity<Long> startChat(@RequestParam String character) {
        Long chatId = chatAIService.createChat(character);  // DB에 Chat 생성 후 id 반환
        return ResponseEntity.ok(chatId);
    }

    // 2. 메시지 추가 + 응답
    @PostMapping("/{chatId}/reply")
    public ResponseEntity<ChatReply2ClientDto> reply(
            @PathVariable Long chatId,
            @RequestBody ChatMessageDto message) throws Exception {

        chatAIService.appendMessage(chatId, message); // DB에 메시지 누적
        ChatReply2ClientDto reply = chatAIService.getReply(chatId); // 누적된 메시지 기반 응답 생성

        return ResponseEntity.ok(reply);
    }

    // 3. 전체 대화 조회
    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long chatId) {
        List<ChatMessageDto> history = chatAIService.getChatMessages(chatId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/diary")
    public ResponseEntity<ChatReply2ClientDto> diary(@RequestParam String character,
                                                     @RequestBody List<ChatMessageDto> messages) throws Exception {
        ChatReply2ClientDto diary = chatAIService.getDiary(character, messages);
        return ResponseEntity.ok(diary);
    }
}

