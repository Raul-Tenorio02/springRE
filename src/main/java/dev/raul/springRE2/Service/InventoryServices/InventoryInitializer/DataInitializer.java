package dev.raul.springRE2.Service.InventoryServices.InventoryInitializer;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Repository.CharacterRepository;
import dev.raul.springRE2.Repository.ItemRepository;
import dev.raul.springRE2.Service.InventoryServices.CharacterInventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final CharacterInventoryService characterInventoryService;

    public DataInitializer(CharacterRepository characterRepository, ItemRepository itemRepository, CharacterInventoryService characterInventoryService) {
        this.characterRepository = characterRepository;
        this.itemRepository = itemRepository;
        this.characterInventoryService = characterInventoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeCharacter("Leon S. Kennedy", "Lighter", "H&K VP70", "Knife");
        initializeCharacter("Claire Redfield", "Lockpick", "Browning HP", "Knife");
        initializeCharacter("Ada Wong", "Ada's Picture", "Browning HP", "Hand Gun Bullets", "First Aid Spray");
        initializeCharacter("Sherry Birkin", "Sherry's Picture", "First Aid Spray");
    }

    private void initializeCharacter(String characterName, String... itemNames) {
        Optional<Character> existingCharacter = characterRepository.findCharacterByName(characterName);
        if (existingCharacter.isEmpty()) {
            Character character = new Character();
            character.setName(characterName);
            characterRepository.save(character);
            initializeInventory(character, itemNames);
        }
    }

    private void initializeInventory(Character character, String... itemNames) {
        for (String itemName : itemNames) {
            itemRepository.findItemByName(itemName)
                    .ifPresent(item -> {
                        if (characterInventoryService.getInventoryByCharacter(character).stream()
                                .noneMatch(inv -> inv.getItem().getId().equals(item.getId()))) {
                            characterInventoryService.addItemToInventory(character, item);
                        }
                    });
        }
    }

}