package com.openketchupsource.soulmate.service.member;

import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.repository.domain.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public Chat initializeChat(Long memberId, Long characterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버가 없습니다."));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 캐릭터가 없습니다."));

        Chat chat = Chat.builder()
                .character(character)
                .build();

        return chatRepository.save(chat);
    }

    @Transactional
    public Diary setDate(Long memberId, int year, int month, int day) { // diary 객체 생성/날짜 지정/저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버가 없습니다."));
        LocalDate date = LocalDate.of(year, month, day);
        Diary diary = Diary.builder().
                member(member)
                .date(date)
                .build();
        return diaryRepository.save(diary);
    }
}