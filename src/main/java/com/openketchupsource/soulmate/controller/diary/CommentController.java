package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.apiPayload.ApiResponse;
import com.openketchupsource.soulmate.converter.CommentConverter;
import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.dto.diary.CommentRequest;
import com.openketchupsource.soulmate.dto.diary.CommentResponse;
import com.openketchupsource.soulmate.service.diary.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // 프론트 측에서 AI 코멘트를 생성하여 넘겨주면 일기에 저장
    @PostMapping("/diary")
    public ApiResponse<CommentResponse.AddCommentDTO> saveComment(@RequestParam Long diaryId, @RequestBody CommentRequest.AddDto request) {
        Comment comment = commentService.AddComment(diaryId, request);
        return ApiResponse.onSuccess(CommentConverter.addCommentDTO(comment.getId()));
    }
}