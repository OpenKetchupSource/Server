package com.openketchupsource.soulmate.repository.diary;

import com.openketchupsource.soulmate.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByName(String name);
}
