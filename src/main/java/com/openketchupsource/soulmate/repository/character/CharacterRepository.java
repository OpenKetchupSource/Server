package com.openketchupsource.soulmate.repository.character;

import com.openketchupsource.soulmate.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Character findByName(String name);
}
