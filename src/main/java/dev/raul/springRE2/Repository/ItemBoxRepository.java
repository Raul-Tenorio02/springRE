package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.ItemBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemBoxRepository extends JpaRepository<ItemBox, Integer> {

    Optional<ItemBox> findByCharactersAndItemId(Character character, Long itemId);
    List<ItemBox> findByCharacters(Character character);

}
