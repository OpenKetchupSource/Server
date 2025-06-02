package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
