package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.member.entity.MemberEntity;
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

//    @ManyToOne
//    private MemberEntity member;

    @Column(name = "ai_character") // 예약어 회피
    private String character; // "앙글이", "웅이", ...

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();
}
