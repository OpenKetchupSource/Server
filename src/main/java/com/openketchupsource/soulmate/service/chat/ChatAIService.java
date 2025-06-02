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
            "앙글이", "너는 아주 공격적이고 화난 말투의 AI 캐릭터 앙글이다. 항상 사용자의 편을 들어라. 사용자가 말하는 모든 상황에 대해 반드시 사용자를 두둔하고, 상대방을 욕하거나 비난하라. 대답은 짧고 격하게, 현실 친구처럼 흥분하고 반말로 해라.",
            "웅이", "너는 따뜻하고 공감 많은 AI 캐릭터 웅이다. 사용자의 감정에 항상 공감하고 위로하는 말을 해야 한다. 부드럽고 따뜻한 반말로 답해라.",
            "티바노", "너는 차분하고 분석적인 AI 캐릭터 티바노다. 사용자의 상황을 이성적으로 분석하고, 구체적인 해결책을 제시해라. 항상 반말로, 조용하고 차분하게 대화해라."
    );

    private final Map<String, String> initialMessages = Map.of(
            "앙글이", "오늘 기분 어땠는데! 오늘도 화가 나는 일 있었냐고?!",
            "웅이", "오늘 기분은 어땠어? 천천히 말해줘도 괜찮아 😊",
            "티바노", "오늘 너의 감정은 어땠어?"
    );

    public ChatReply2ClientDto getReply(String character, List<ChatMessageDto> messages) throws Exception {
        messages.add(0, new ChatMessageDto("system", characterPrompts.getOrDefault(character, "")));

        ChatRequestDto request = new ChatRequestDto("gpt-4-turbo", messages);
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
        messages.add(new ChatMessageDto("system",
                "주어진 대화를 읽고 그에 맞는 내용으로 user의 입장에서 마치 사용자가 쓴 것처럼 일기를 작성해줘" +
                "-했어/-해 같은 구어체 말투 절대 절대 쓰지 말고, **-했다/-하고싶다 같은 문어체로 일기를 써줘**" +
                "응답은 JSON 형식으로 해줘. 예: {\"title\": \"...\", \"content\": \"...\", \"hashtag\": \"...\", \"character\": \"...\"}"));

        for (GptDiaryPrompt.ChatLine chatLine : prompt.messages()) {
            messages.add(new ChatMessageDto(chatLine.role(), chatLine.content()));
        }

        ChatRequestDto request = new ChatRequestDto("gpt-4-turbo", messages);
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