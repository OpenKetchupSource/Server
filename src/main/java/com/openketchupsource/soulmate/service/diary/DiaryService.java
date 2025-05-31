package com.openketchupsource.soulmate.service.diary;

import com.openketchupsource.soulmate.domain.*;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.dto.diary.*;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.chat.ChatMessageRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import com.openketchupsource.soulmate.repository.diary.DiaryRepository;
import com.openketchupsource.soulmate.repository.diary.HashTagRepository;
import com.openketchupsource.soulmate.service.chat.ChatAIService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
                diary.getId(),
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

    @Transactional
    public ClientDiaryResponse createDiary(ClientDiaryCreateRequest request, Member member) {
        List<HashTag> tags = parseHashtags(request.hashtag());

        Character character = characterRepository.findByName(request.character().trim())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다: " + request.character()));

        Diary diary = Diary.builder()
                .title(request.title())
                .content(request.content())
                .date(request.date())
                .member(member)
                .character(character)
                .hashtags(tags)
                .build();

        // 양방향 연관관계 유지
        tags.forEach(tag -> tag.addDiary(diary)); // tag.diaries에 diary 추가

        diaryRepository.save(diary);

        return new ClientDiaryResponse(
                diary.getId(),
                request.date(),
                request.title(),
                request.content(),
                request.hashtag(),
                request.character()
        );
    }

    @Transactional
    public List<DiaryListResponse> getMemberDiaryList(Member member) {
        List<Diary> diaryList = diaryRepository.findByMember(member).stream().toList();

        List<DiaryListResponse> diaryListResponseList = new ArrayList<>();
        for (Diary diary : diaryList) {
            diaryListResponseList.add(DiaryListResponse.of(diary.getId(), diary.getDate(), diary.getTitle(), diary.getContent(), diary.getHashtags().stream().map(HashTag::getName).toList()));
        }

        diaryListResponseList.sort(
                Comparator
                        .comparing(DiaryListResponse::date)
                        .reversed()
                        .thenComparing(DiaryListResponse::id, Comparator.reverseOrder())
        );

        return diaryListResponseList;
    }

    @Transactional
    public DiaryResponse getDiaryById(Member member, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일기입니다. " + diaryId));
        if (!diary.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 멤버의 일기가 아닙니다.");
        }

        if (diary.getComment() == null) {
            return DiaryResponse.of(diary.getId(), diary.getDate(), diary.getTitle(), diary.getContent(), null, diary.getCharacter().getName(), diary.getHashtags().stream().map(HashTag::getName).toList());
        }
        return DiaryResponse.of(diary.getId(), diary.getDate(), diary.getTitle(), diary.getContent(), diary.getComment().getContext(), diary.getCharacter().getName(), diary.getHashtags().stream().map(HashTag::getName).toList());
    }

    @Transactional
    public ClientDiaryResponse updateDiary(Long diaryId, Member member, ClientDiaryUpdateRequest request) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일기입니다."));

        if (!diary.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 멤버의 일기가 아닙니다.");
        }

        // 기존 해시태그 다 제거하고 새로 추가
        diary.getHashtags().clear();

        List<HashTag> newTags = parseHashtags(request.hashtag());
        newTags.forEach(tag -> diary.addHashtag(tag));

        // 필드 업데이트
        diary.setDate(request.date());
        diary.setTitle(request.title());
        diary.setContent(request.content());

        // 저장
        diaryRepository.save(diary);

        return new ClientDiaryResponse(
                diary.getId(),
                diary.getDate(),
                diary.getTitle(),
                diary.getContent(),
                request.hashtag(),
                diary.getCharacter().getName() // 캐릭터는 수정 안 하니까 그대로
        );
    }

    @Transactional
    public void deleteDiary(Long diaryId, Member member) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일기입니다."));

        if (!diary.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 멤버의 일기가 아닙니다.");
        }

        diaryRepository.delete(diary);
    }
}
