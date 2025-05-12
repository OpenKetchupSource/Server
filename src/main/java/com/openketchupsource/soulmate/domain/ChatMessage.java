package com.openketchupsource.soulmate.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Chat chat;

    private String role;    // user, assistant, system
    private String content;
}
