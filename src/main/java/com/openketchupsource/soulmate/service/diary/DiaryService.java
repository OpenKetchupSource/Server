package com.openketchupsource.soulmate.service.diary;

import com.openketchupsource.soulmate.domain.*;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.dto.diary.ClientGptDiaryCreateRequest;
import com.openketchupsource.soulmate.dto.diary.GptDiaryPrompt;
import com.openketchupsource.soulmate.dto.diary.GptDiaryResponse;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.chat.ChatMessageRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.repository.diary.HashTagRepository;
import com.openketchupsource.soulmate.service.chat.ChatAIService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CharacterRepository characterRepository;
    private final HashTagRepository hashTagRepository;
    private final ChatAIService gptClient;

    private void setDate(Diary diary, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        diary.setDate(date);
    }

    // ai 채팅 보고 캐릭터가 일기 생성
    @Transactional
    public GptDiaryResponse createDiaryFromChat(ClientGptDiaryCreateRequest request, Member member) throws Exception {
        // Chat 조회
        Chat chat = chatRepository.findById(request.chatId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Chat 없음"));

        // 해당 Chat(대화창) 메시지 조회
        List<ChatMessage> messages = chatMessageRepository
                .findByChatIdOrderByCreatedAtAsc(request.chatId());

        // GPT 프롬프트 생성
        List<GptDiaryPrompt.ChatLine> chatLines = messages.stream()
                .map(m -> new GptDiaryPrompt.ChatLine(m.getRole(), m.getContent()))
                .toList();

        GptDiaryPrompt prompt = new GptDiaryPrompt(request.character(), chatLines);

        // GPT API 호출
        GptDiaryResponse response = gptClient.generateDiary(prompt);

        // 캐릭터 엔티티 조회
        Character character = characterRepository.findByName(request.character().trim())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다: " + request.character()));

        // 해시태그 파싱
        List<HashTag> tags = parseHashtags(response.hashtag());

        // Diary 생성
        Diary diary = Diary.builder()
                .title(response.title())
                .content(response.content())
                .date(request.date())
                .member(member)
                .character(character)
                .hashtags(tags) // Builder로 addAll 처리됨
                .build();

        // 양방향 연관관계 유지
        tags.forEach(tag -> tag.addDiary(diary)); // tag.diaries에 diary 추가

        diaryRepository.save(diary);

        return new GptDiaryResponse(
                response.title(),
                response.content(),
                response.hashtag(),
                request.character()
        );
    }

    // 지피티가 반환한 Hashtag들을 파싱해서 디비에 저장할 수 있게 하는 역할임다
    private List<HashTag> parseHashtags(String hashtagStr) {
        return Arrays.stream(hashtagStr.split("[\\s,]+"))
                .map(tag -> tag.replace("#", "").trim())
                .filter(tag -> !tag.isBlank())
                .distinct()
                .map(this::findOrCreateHashTag)
                .toList();
    }

    private HashTag findOrCreateHashTag(String name) {
        return hashTagRepository.findByName(name)
                .orElseGet(() -> hashTagRepository.save(new HashTag(name)));
    }
}
