package com.openketchupsource.soulmate.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    private Long diaryId;
    private Long hashtagId;
}
