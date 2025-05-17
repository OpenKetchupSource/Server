package com.openketchupsource.soulmate.service.member;

import com.openketchupsource.soulmate.apiPayload.exception.handler.LoginHandler;
import com.openketchupsource.soulmate.apiPayload.exception.handler.SettingHandler;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.Member;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.repository.member.MemberRepository;
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
                .orElseThrow(() -> new SettingHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new SettingHandler(ErrorStatus.CHARACTER_NOT_FOUND));

        Chat chat = Chat.builder()
                .character(character)
                .build();

        return chatRepository.save(chat);
    }

    @Transactional
    public Diary setDate(Long memberId, int year, int month, int day) { // diary 객체 생성/날짜 지정/저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SettingHandler(ErrorStatus.MEMBER_NOT_FOUND));
        LocalDate date = LocalDate.of(year, month, day);
        Diary diary = Diary.builder().
                member(member)
                .date(date)
                .build();
        return diaryRepository.save(diary);
    }
}