package com.openketchupsource.soulmate.service.member;

import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.repository.domain.CharacterRepository;
import com.openketchupsource.soulmate.repository.domain.ChatRepository;
import com.openketchupsource.soulmate.repository.domain.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;

    @Transactional
    public Chat initializeChat(Long memberId, Long characterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버가 없습니다."));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 캐릭터가 없습니다."));

        Chat chat = Chat.builder()
                .member(member)
                .character(character)
                .build();

        return chatRepository.save(chat);
    }
}