package com.openketchupsource.soulmate.repository.domain;

import com.openketchupsource.soulmate.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
