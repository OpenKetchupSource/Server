package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.member.entity.Diary;
import com.openketchupsource.soulmate.member.entity.HashTag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DiaryToHashtag {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtagId", nullable = false)
    private HashTag hashtag;
}
