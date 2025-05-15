package com.openketchupsource.soulmate.repository.domain;

import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
