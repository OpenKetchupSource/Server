package com.openketchupsource.soulmate.service.diary;

import com.openketchupsource.soulmate.apiPayload.exception.handler.DiaryHandler;
import com.openketchupsource.soulmate.converter.CommentConverter;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.dto.diary.CommentListResponse;
import com.openketchupsource.soulmate.dto.diary.CommentRequest;
import com.openketchupsource.soulmate.dto.diary.DiaryListResponse;
import com.openketchupsource.soulmate.dto.diary.StoredCommentResponse;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.diary.CommentRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final DiaryRepository diaryRepository;
    private final CharacterRepository characterRepository;

    @Transactional
    public Comment AddComment (Long diaryId, CommentRequest.AddDto request ) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        Character character = characterRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new DiaryHandler(ErrorStatus.CHARACTER_NOT_FOUND));

        Comment newComment = CommentConverter.toComment(request.getContext(), character, diary);
        commentRepository.save(newComment);

        if (diary.getComment() == null) {
            diary.setComment(newComment);
        }else {
            throw new DiaryHandler(ErrorStatus.COMMENT_ALREADY_EXIST);
        }
        return newComment;
    }

    @Transactional
    public StoredCommentResponse bookmarkComment (Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("코멘트가 존재하지 않습니다. " + commentId)
        );

        if (!comment.getDiary().getMember().equals(member)) {
            throw new IllegalArgumentException("사용자의 일기에 달린 코멘트가 아닙니다. " + commentId);
        }

        if (comment.getIsStored() == 0) {
            comment.setIsStored(1);
        } else {
            comment.setIsStored(0);
        }

        commentRepository.save(comment);
        return StoredCommentResponse.of(comment.getId(), comment.getContext(), comment.getIsStored(), comment.getCharacter().getName());
    }

    @Transactional
    public List<CommentListResponse> getBookmarkedComments (Member member, Long characterId) {
        Character character = characterRepository.findById(characterId).orElseThrow(
                () -> new DiaryHandler(ErrorStatus.CHARACTER_NOT_FOUND)
        );
        List<Comment> commentList = commentRepository.findByCharacter_NameAndIsStored(character.getName(), 1);

        commentList.removeIf(comment -> !comment.getDiary().getMember().equals(member));
        List<CommentListResponse> responses = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentListResponse response = CommentListResponse.of(comment.getId(), comment.getContext(), comment.getIsStored(), comment.getCharacter().getName(), comment.getDiary().getDate());
            responses.add(response);
        }

        responses.sort(Comparator
                .comparing(CommentListResponse::date)
                .reversed()
                .thenComparing(CommentListResponse::id, Comparator.reverseOrder()));
        return responses;
    }

}
