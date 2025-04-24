package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.ItemBox;
import dev.raul.springRE2.Repository.ItemBoxRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemBoxService {

    private final ItemBoxRepository itemBoxRepository;

    public ItemBoxService(ItemBoxRepository itemBoxRepository) {
        this.itemBoxRepository = itemBoxRepository;
    }

    public void addToItemBox(ItemBox itemBox) {
        itemBoxRepository.save(itemBox);
    }

    public Optional<ItemBox> getInItemBoxById(Integer id) {
        return itemBoxRepository.findById(id);
    }

    public List<ItemBox> getItemBoxByCharacter(Character character) {
        return itemBoxRepository.findByCharacters(character);
    }

    public Optional<ItemBox> findByCharacterAndItemId (Character character, Long itemId) {
        return itemBoxRepository.findByCharactersAndItemId(character, itemId);
    }

    public void delete(ItemBox itemBox) {
        itemBoxRepository.delete(itemBox);
    }

}
