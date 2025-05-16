package com.openketchupsource.soulmate.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public Chat(Character character) {
        this.character = character;
        this.messages = new ArrayList<>();
    }

    // 메시지 추가 메서드 (양방향 연관관계 편의)
    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setChat(this);
    }
}
