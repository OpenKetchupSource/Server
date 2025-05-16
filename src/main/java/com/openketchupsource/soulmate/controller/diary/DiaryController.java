package com.openketchupsource.soulmate.controller.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    // 프론트 측에서 chatId 입력해서 넣으면?? DB에 있는 해당 대화 GPT API에게 넘겨주고 일기 생성하도록
}
