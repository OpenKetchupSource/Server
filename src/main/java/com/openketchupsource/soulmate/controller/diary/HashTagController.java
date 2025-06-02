package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.dto.diary.HashTagDTO;
import com.openketchupsource.soulmate.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hashtag")
public class HashTagController {
    private final DiaryService diaryService;

    @GetMapping("/names")
    public ResponseEntity<List<HashTagDTO>> showAllHashTags() {
        List<HashTagDTO> AllHashTags = diaryService.findAllHashTags();
        return ResponseEntity.ok(AllHashTags);
    }
}