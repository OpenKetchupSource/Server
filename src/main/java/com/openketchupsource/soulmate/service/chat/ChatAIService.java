package com.openketchupsource.soulmate.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openketchupsource.soulmate.domain.*;
import com.openketchupsource.soulmate.domain.Character;
import com.openketchupsource.soulmate.dto.chat.*;
import com.openketchupsource.soulmate.dto.diary.GptDiaryPrompt;
import com.openketchupsource.soulmate.dto.diary.GptDiaryResponse;
import com.openketchupsource.soulmate.repository.character.CharacterRepository;
import com.openketchupsource.soulmate.repository.chat.ChatMessageRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import com.openketchupsource.soulmate.repository.diary.HashTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatAIService {

    @Value("${openai.secret-key}")
    private String apiKey;

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CharacterRepository characterRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final Map<String, String> characterPrompts = Map.of(
            "앙글이", "너는 아주 공격적이고 화난 말투의 AI 캐릭터 앙글이야. 무조건 사용자 편을 들어주고, 사용자의 화나는 상황에 대신 화내주면 돼. 현실 친구같은 느낌으로 흥분하고 격분하고 반말하면서!",
            "웅이", "너는 따뜻하고 공감 많은 AI 캐릭터 웅이야. 사용자의 감정을 잘 위로해줘.",
            "티바노", "너는 차분하고 분석적인 AI 캐릭터 티바노야. 상황을 이성적으로 판단해서 해결책을 제시해줘. 대화하는 것처럼 말해줘. 반말로 해줘."
    );

    private final Map<String, String> initialMessages = Map.of(
            "앙글이", "오늘 기분 어땠는데! 오늘도 화가 나는 일 있었냐고?!",
            "웅이", "오늘 기분은 어땠어? 천천히 말해줘도 괜찮아 😊",
            "티바노", "오늘 너의 감정은 어땠어?"
    );

    public ChatReply2ClientDto getReply(String character, List<ChatMessageDto> messages) throws Exception {
        messages.add(0, new ChatMessageDto("system", characterPrompts.getOrDefault(character, "")));

        ChatRequestDto request = new ChatRequestDto("gpt-3.5-turbo", messages);
        HttpPost post = new HttpPost(API_URL);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(mapper.writeValueAsString(request), StandardCharsets.UTF_8));

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post);
             InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)) {

            ChatResponseDto chatResponse = mapper.readValue(reader, ChatResponseDto.class);
            ChatResponseDto.Message replyMessage = chatResponse.getChoices().get(0).getMessage();

            return new ChatReply2ClientDto(replyMessage.getRole(), replyMessage.getContent());
        }
    }

    public ChatReply2ClientDto getDiary(String character, List<ChatMessageDto> messages) throws Exception {
        messages.add(new ChatMessageDto("system", characterPrompts.getOrDefault(character, "") + " 지금까지의 대화를 바탕으로 일기를 작성해줘."));
        return getReply(character, messages);
    }

    @Transactional
    public ChatInitResponseDto createChat(String characterName) {
        Character character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다: " + characterName));;
        Chat chat = Chat.builder().character(character).build();
        chatRepository.save(chat);

        String initMessage = initialMessages.getOrDefault(character.getName(), "오늘 기분 어땠어?");

        ChatMessage message = ChatMessage.builder()
                .chat(chat)
                .role("assistant")
                .content(initMessage)
                .build();
        chatMessageRepository.save(message);

        return new ChatInitResponseDto(chat.getId(), "assistant", initMessage);
    }

    @Transactional
    public void appendMessage(Long chatId, ChatMessageDto messageDto) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));

        ChatMessage message = ChatMessage.builder()
                .chat(chat)
                .role(messageDto.getRole())
                .content(messageDto.getContent())
                .build();

        chatMessageRepository.save(message);
    }

    public ChatReply2ClientDto getReply(Long chatId) throws Exception {
        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);
        List<ChatMessageDto> dtoList = messages.stream()
                .map(m -> new ChatMessageDto(m.getRole(), m.getContent()))
                .collect(Collectors.toCollection(ArrayList::new));

        String character = messages.get(0).getChat().getCharacter().getName();
        Chat chat = messages.get(0).getChat();

        // GPT로 응답 생성
        ChatReply2ClientDto reply = getReply(character, dtoList);

        // 저장
        ChatMessage aiMessage = ChatMessage.builder()
                .chat(chat)
                .role(reply.getRole())
                .content(reply.getContent())
                .build();
        chatMessageRepository.save(aiMessage);

        return reply;
    }

    public List<ChatMessageDto> getChatMessages(Long chatId) {
        return chatMessageRepository.findByChatIdOrderByCreatedAtAsc(chatId).stream()
                .map(m -> new ChatMessageDto(m.getRole(), m.getContent()))
                .toList();
    }

    // 일기 생성 로직
    public GptDiaryResponse generateDiary(GptDiaryPrompt prompt) throws Exception {
        // GPT API 요청용 메시지 구성
        List<ChatMessageDto> messages = new ArrayList<>();
        messages.add(new ChatMessageDto("system", characterPrompts.getOrDefault(prompt.character(), "") +
                "지금까지의 대화를 바탕으로 사용자의 하루를 요약한 **일기**를 작성해줘. " +
                "반드시 **문어체**로 작성해야 하며, **-했다, -였다** 같은 표현을 사용해야 해. " +
                "**-했어, -였어, -거야**와 같은 말투는 절대 쓰지 마. " +
                "응답은 JSON 형식으로 해줘. 예: {\"title\": \"...\", \"content\": \"...\", \"hashtag\": \"...\", \"character\": \"...\"}"));

        for (GptDiaryPrompt.ChatLine chatLine : prompt.messages()) {
            messages.add(new ChatMessageDto(chatLine.role(), chatLine.content()));
        }

        ChatRequestDto request = new ChatRequestDto("gpt-3.5-turbo", messages);
        // 지피티가 뻘소리 하는 경우가 있어서 while 문으로 검토하기
//        HttpPost post = new HttpPost(API_URL);
//        post.setHeader("Authorization", "Bearer " + apiKey);
//        post.setHeader("Content-Type", "application/json");
//        post.setEntity(new StringEntity(mapper.writeValueAsString(request), StandardCharsets.UTF_8));
//
//        try (CloseableHttpClient client = HttpClients.createDefault();
//             CloseableHttpResponse response = client.execute(post);
//             InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)) {
//
//            ChatResponseDto chatResponse = mapper.readValue(reader, ChatResponseDto.class);
//            String contentJson = chatResponse.getChoices().get(0).getMessage().getContent();
//
//            // JSON으로 된 일기 응답 파싱
//            return mapper.readValue(contentJson, GptDiaryResponse.class);
        int retryCount = 0;
        final int maxRetries = 3;   // 최대 3번 다시 진행

        while (retryCount < maxRetries) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(mapper.writeValueAsString(request), StandardCharsets.UTF_8));

            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(post);
                 InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)) {

                ChatResponseDto chatResponse = mapper.readValue(reader, ChatResponseDto.class);
                String contentJson = chatResponse.getChoices().get(0).getMessage().getContent();

                try {
                    return mapper.readValue(contentJson, GptDiaryResponse.class); // JSON이 맞다면 return
                } catch (Exception e) {
                    retryCount++;
                    System.err.println("[GPT 일기 파싱 실패] 응답 내용: " + contentJson);
                    if (retryCount == maxRetries) throw new RuntimeException("GPT 응답이 유효한 JSON이 아님", e);
                }

            }
        }

        throw new RuntimeException("GPT 응답 파싱 실패");
    }
}