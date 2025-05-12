package com.openketchupsource.soulmate.repository.chat;

import com.openketchupsource.soulmate.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
