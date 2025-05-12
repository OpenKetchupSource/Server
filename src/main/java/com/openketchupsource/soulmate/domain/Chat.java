package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MemberEntity member;

    private String character; // "앙글이", "웅이", ...

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();
}
