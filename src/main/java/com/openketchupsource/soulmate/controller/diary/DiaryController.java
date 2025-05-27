package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.apiPayload.ApiResponse;
import com.openketchupsource.soulmate.auth.PrincipalHandler;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.dto.diary.*;
import com.openketchupsource.soulmate.service.diary.DiaryService;
import com.openketchupsource.soulmate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {
    private final DiaryService diaryService;
    private final MemberService memberService;

    // 프론트 측에서 chatId 입력해서 넣으면?? DB에 있는 해당 대화 GPT API에게 넘겨주고 일기 생성하도록
    @PostMapping("/generate")
    public ResponseEntity<GptDiaryResponse> generateDiary(@RequestBody ClientGptDiaryCreateRequest request) throws Exception {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        GptDiaryResponse gptResponse = diaryService.createDiaryFromChat(request, member);
        return ResponseEntity.ok(gptResponse);
    }

    // 사용자가 직접 일기를 작성하는 경우
    @PostMapping("/create")
    public ResponseEntity<ClientDiaryResponse> createDiary(@RequestBody ClientDiaryCreateRequest request) {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        ClientDiaryResponse response = diaryService.createDiary(request, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<DiaryListResponse>> getDiaryListByMember() {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        List<DiaryListResponse> responses = diaryService.getMemberDiaryList(member);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryById(@RequestParam Long diaryId) {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        DiaryResponse response = diaryService.getDiaryById(member, diaryId);
        return ResponseEntity.ok(response);
    }
}
