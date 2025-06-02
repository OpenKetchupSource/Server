package com.openketchupsource.soulmate.domain.mapping;

import com.openketchupsource.soulmate.domain.Diary;
import com.openketchupsource.soulmate.domain.HashTag;
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
