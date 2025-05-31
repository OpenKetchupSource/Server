package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false )
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true )
    private Comment comment;

    // 코멘트를 단 ai 캐릭터가 누구인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    // 해시태그 (다대다니까 중간테이블 만들게여,,)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "diary_hashtag",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<HashTag> hashtags = new ArrayList<>();

    public void addHashtag(HashTag tag) {
        hashtags.add(tag);
        tag.addDiary(this);
    }

    @Builder
    public Diary(LocalDate date, String title, String content, Member member, Character character, List<HashTag> hashtags) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.member = member;
        this.character = character;
        if (hashtags != null) {
            this.hashtags.addAll(hashtags);
        }
    }
}
