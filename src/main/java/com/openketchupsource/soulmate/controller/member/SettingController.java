package com.openketchupsource.soulmate.controller.member;

import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.service.member.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/setting")
public class SettingController {
    private final SettingService settingService;

    @PostMapping("/initialize")
    public ResponseEntity<Chat> initializeChat(@RequestParam Long memberId,
                                               @RequestParam Long characterId) {
        Chat createdChat = settingService.initializeChat(memberId, characterId);
        return ResponseEntity.ok(createdChat);
    }
}
