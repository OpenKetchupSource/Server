package com.openketchupsource.soulmate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hashtag")
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "hashtags")
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public HashTag(String name) {
        this.name = name;
    }

    public void addDiary(Diary diary) {
        diaries.add(diary);
    }
}
