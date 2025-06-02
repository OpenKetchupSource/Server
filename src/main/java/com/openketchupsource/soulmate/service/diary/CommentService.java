package com.openketchupsource.soulmate.service.diary;

import com.openketchupsource.soulmate.apiPayload.exception.handler.DiaryHandler;
import com.openketchupsource.soulmate.converter.CommentConverter;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.dto.diary.CommentRequest;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.diary.CommentRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
