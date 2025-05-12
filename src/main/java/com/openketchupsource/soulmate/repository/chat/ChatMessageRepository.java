package com.openketchupsource.soulmate.repository.chat;

import com.openketchupsource.soulmate.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> { }
