package com.openketchupsource.soulmate.converter;

import com.openketchupsource.soulmate.apiPayload.exception.handler.DiaryHandler;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Comment;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.dto.diary.CommentResponse;

import java.time.LocalDateTime;

public class CommentConverter {
    public static CommentResponse.AddCommentDTO addCommentDTO (Long commentId){
        return CommentResponse.AddCommentDTO.builder()
                .commentId(commentId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Comment toComment(String comment, Character character, Diary diary){
        if (comment == null || character == null || diary == null) {
            throw new DiaryHandler(ErrorStatus._NULL_JSON);
        }else{
            return Comment.builder()
                    .context(comment)
                    .character(character)
                    .diary(diary)
                    .build();
        }
    }
}
