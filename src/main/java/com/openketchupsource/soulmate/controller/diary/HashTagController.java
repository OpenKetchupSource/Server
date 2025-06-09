package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.auth.PrincipalHandler;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.dto.diary.HashTagDTO;
import com.openketchupsource.soulmate.service.diary.DiaryService;
import com.openketchupsource.soulmate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hashtag")
public class HashTagController {
    private final DiaryService diaryService;
    private final MemberService memberService;

    @GetMapping("/names")
    public ResponseEntity<List<HashTagDTO>> showAllHashTags() {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        List<HashTagDTO> AllHashTags = diaryService.findAllHashTags(member);
        return ResponseEntity.ok(AllHashTags);
    }
}