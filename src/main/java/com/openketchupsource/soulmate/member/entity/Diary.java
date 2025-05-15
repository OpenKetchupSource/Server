package com.openketchupsource.soulmate.member.entity;

import com.openketchupsource.soulmate.domain.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @JoinColumn(name = "comment_id", nullable = false )
    private Comment comment;

    @Builder
    public Diary(LocalDate date, String title, String content, Member member, Comment comment) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
