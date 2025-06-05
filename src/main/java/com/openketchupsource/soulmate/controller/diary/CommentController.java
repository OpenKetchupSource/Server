package com.openketchupsource.soulmate.controller.diary;

import com.openketchupsource.soulmate.apiPayload.ApiResponse;
import com.openketchupsource.soulmate.auth.PrincipalHandler;
import com.openketchupsource.soulmate.converter.CommentConverter;
import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.dto.diary.CommentListResponse;
import com.openketchupsource.soulmate.dto.diary.CommentRequest;
import com.openketchupsource.soulmate.dto.diary.CommentResponse;
import com.openketchupsource.soulmate.dto.diary.StoredCommentResponse;
import com.openketchupsource.soulmate.service.diary.CommentService;
import com.openketchupsource.soulmate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;

    // 프론트 측에서 AI 코멘트를 생성하여 넘겨주면 일기에 저장
    @PostMapping("/diary")
    public ApiResponse<CommentResponse.AddCommentDTO> saveComment(@RequestParam Long diaryId, @RequestBody CommentRequest.AddDto request) {
        Comment comment = commentService.AddComment(diaryId, request);
        return ApiResponse.onSuccess(CommentConverter.addCommentDTO(comment.getId()));
    }

    @PostMapping("/bookmark")
    public ApiResponse<StoredCommentResponse> bookmarkComment(@RequestParam Long commentId) {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        StoredCommentResponse comment = commentService.bookmarkComment(commentId, member);
        return ApiResponse.onSuccess(comment);
    }

    @GetMapping("/bookmark/get")
    public ApiResponse<List<CommentListResponse>> getBookmarkComments(@RequestParam Long characterId) {
        Long memberId = PrincipalHandler.getMemberIdFromPrincipal();
        Member member = memberService.findById(memberId);
        List<CommentListResponse> responses = commentService.getBookmarkedComments(member, characterId);
        return ApiResponse.onSuccess(responses);
    }
}