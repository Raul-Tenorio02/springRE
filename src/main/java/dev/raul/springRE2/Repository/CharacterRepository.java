package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Inventories.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
}
