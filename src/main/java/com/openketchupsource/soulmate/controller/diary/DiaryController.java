package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.auth.PrincipalHandler;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.dto.diary.ClientGptDiaryCreateRequest;
import com.openketchupsource.soulmate.repository.domain.MemberRepository;
import com.openketchupsource.soulmate.service.diary.DiaryService;
import com.openketchupsource.soulmate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {
    private final DiaryService diaryService;
    private final MemberService memberService;

    // 프론트 측에서 chatId 입력해서 넣으면?? DB에 있는 해당 대화 GPT API에게 넘겨주고 일기 생성하도록
    @PostMapping("/generate")
    public ResponseEntity<?> generateDiary(
            @RequestBody ClientGptDiaryCreateRequest request
    ) throws Exception {
        log.info("다이어리 생성 요청 들어옴: " + request.toString());

        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(diaryService.createDiaryFromChat(request, member));
    }
}
