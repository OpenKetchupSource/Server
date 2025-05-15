package com.openketchupsource.soulmate.domain;

import com.openketchupsource.soulmate.member.entity.BaseTimeEntity;
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
    @JoinColumn(name = "id", nullable = false)
    private Character character;
}
