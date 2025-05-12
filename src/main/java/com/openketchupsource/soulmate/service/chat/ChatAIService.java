package com.openketchupsource.soulmate.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.ChatMessage;
import com.openketchupsource.soulmate.dto.chat.ChatMessageDto;
import com.openketchupsource.soulmate.dto.chat.ChatReply2ClientDto;
import com.openketchupsource.soulmate.dto.chat.ChatRequestDto;
import com.openketchupsource.soulmate.dto.chat.ChatResponseDto;
import com.openketchupsource.soulmate.repository.chat.ChatMessageRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
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
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final Map<String, String> characterPrompts = Map.of(
            "앙글이", "너는 아주 공격적이고 화난 말투의 AI 캐릭터 앙글이야. 무조건 사용자 편을 들어주고, 사용자의 화나는 상황에 대신 화내주면 돼. 현실 친구같은 느낌으로 흥분하고 격분하고 반말하면서!",
            "웅이", "너는 따뜻하고 공감 많은 AI 캐릭터 웅이야. 사용자의 감정을 잘 위로해줘.",
            "티바노", "너는 차분하고 분석적인 AI 캐릭터 티바노야. 상황을 이성적으로 판단해서 해결책을 제시해줘."
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
    public Long createChat(String character) {
        Chat chat = Chat.builder().character(character).build();
        chatRepository.save(chat);
        return chat.getId();
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

        String character = messages.get(0).getChat().getCharacter();
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
}