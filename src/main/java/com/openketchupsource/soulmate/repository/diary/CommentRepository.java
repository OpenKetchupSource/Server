package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCharacter_NameAndIsStored(String characterName, int isStored);
    void deleteAllByDiaryId(Long diaryId);
}
