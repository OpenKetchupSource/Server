package com.openketchupsource.soulmate.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String context;

    @Column(columnDefinition = "int default '0'", nullable = false)
    private int isStored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characterId", nullable = false)
    private Character character;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    @Builder
    public Comment(String context, Character character) {
        this.context = context;
        this.character = character;
    }
}
