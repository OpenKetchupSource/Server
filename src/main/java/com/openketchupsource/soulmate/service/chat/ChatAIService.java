package com.openketchupsource.soulmate.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openketchupsource.soulmate.domain.Chat;
import com.openketchupsource.soulmate.domain.ChatMessage;
import com.openketchupsource.soulmate.dto.chat.ChatMessageDto;
import com.openketchupsource.soulmate.dto.chat.ChatReply2ClientDto;
import com.openketchupsource.soulmate.dto.chat.ChatRequestDto;
import com.openketchupsource.soulmate.dto.chat.ChatResponseDto;
import com.openketchupsource.soulmate.member.entity.MemberEntity;
import com.openketchupsource.soulmate.repository.chat.ChatMessageRepository;
import com.openketchupsource.soulmate.repository.chat.ChatRepository;
import jakarta.transaction.Transactional;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map;

@Service
public class ChatAIService {

    @Value("${openai.secret-key}")
    private String apiKey;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, String> characterPrompts = Map.of(
            "앙글이", "너는 아주 공격적이고 화난 말투의 AI 캐릭터 앙글이야. 무조건 사용자 편을 들어주고, 사용자의 화나는 상황에 함께 분노해줘. 엄청 흥분하면서 격분하면서 말해야 돼.",
            "웅이", "너는 따뜻하고 공감 많은 AI 캐릭터 웅이야. 사용자의 감정을 잘 위로해줘. 존댓말을 쓰는 아주 친절한 말투야.",
            "티바노", "너는 차분하고 분석적인 AI 캐릭터 티바노야. 사용자의 상황을 이성적으로 판단해서 해결책을 제시해줘."
    );

    public ChatReply2ClientDto getReply(String character, List<ChatMessageDto> messages) throws Exception {
        messages.add(0, new ChatMessageDto("system", characterPrompts.getOrDefault(character, "")));

        ChatRequestDto request = new ChatRequestDto("gpt-3.5-turbo", messages);

        HttpPost post = new HttpPost(API_URL);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(mapper.writeValueAsString(request), StandardCharsets.UTF_8)); // 🔥 여기가 핵심

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post);
             InputStreamReader streamReader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)) {

            ChatResponseDto chatResponse = mapper.readValue(streamReader, ChatResponseDto.class);
            ChatResponseDto.Choice choice = chatResponse.getChoices().get(0);
            ChatResponseDto.Message message = choice.getMessage();

            return new ChatReply2ClientDto(message.getRole(), message.getContent());
        }
    }

    public ChatReply2ClientDto getDiary(String character, List<ChatMessageDto> messages) throws Exception {
        messages.add(new ChatMessageDto("system", characterPrompts.getOrDefault(character, "")
                + " 지금까지의 대화를 바탕으로 너의 말투를 반영한 일기를 써줘."));

        return getReply(character, messages); // 이제 반환 타입이 맞음
    }

    // 대화 저장
    @Transactional
    public Chat saveChatWithMessages(String character, MemberEntity member, List<ChatMessageDto> messageDtos) {
        Chat chat = new Chat();
        chat.setCharacter(character);
        chat.setMember(member);

        for (ChatMessageDto dto : messageDtos) {
            ChatMessage message = new ChatMessage();
            message.setChat(chat);
            message.setRole(dto.getRole());
            message.setContent(dto.getContent());
            chat.getMessages().add(message);
        }

        return chatRepository.save(chat); // CascadeType.ALL이므로 메시지도 같이 저장됨
    }
}

