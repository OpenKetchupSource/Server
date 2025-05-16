package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.domain.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false )
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true )
    private Comment comment;

    // 일기를 쓴 ai 캐릭터가 누구인지 or 코멘트를 단 ai 캐릭터가 누구인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Builder
    public Diary(LocalDate date, String title, String content, Member member, Character character) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.member = member;
        this.character = character;
    }
}
