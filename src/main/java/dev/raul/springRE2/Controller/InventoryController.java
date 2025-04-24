package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Model.Inventories.ItemBox;
import dev.raul.springRE2.Service.InventoryServices.CharacterService;
import dev.raul.springRE2.Service.InventoryServices.CharacterInventoryService;
import dev.raul.springRE2.Service.InventoryServices.ItemBoxService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{characterName}/inventory")
public class InventoryController {

    private final CharacterInventoryService characterInventoryService;
    private final ItemBoxService itemBoxService;
    private final CharacterService characterService;

    public InventoryController(CharacterInventoryService inventoryService, ItemBoxService itemBoxService, CharacterService characterService) {
        this.characterInventoryService = inventoryService;
        this.itemBoxService = itemBoxService;
        this.characterService = characterService;
    }

    @GetMapping
    public List<Inventory> getInventory(@PathVariable String characterName) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        return characterInventoryService.getInventoryByCharacter(character);
    }

    @GetMapping("/itembox")
    public List<ItemBox> getItemBox(@PathVariable String characterName) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        return itemBoxService.getItemBoxByCharacter(character);
    }

    @PostMapping("/collect/{itemId}")
    public void collectItem(@PathVariable String characterName, @PathVariable Long itemId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.collectItem(character, itemId);
    }

    @PostMapping("/transfer/{itemId}")
    public void transferItem(@PathVariable String characterName, @PathVariable Long itemId, @RequestParam boolean toItemBox) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.itemBoxTransfer(character, itemId, toItemBox);
    }

    @PostMapping("/use/{weaponId}")
    public void useWeapon(@PathVariable String characterName, @PathVariable Long weaponId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.useWeapon(character, weaponId);
    }

    @GetMapping("/file/{fileId}")
    public String readFile(@PathVariable String characterName, @PathVariable Long fileId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        return characterInventoryService.readFile(character, fileId);
    }

    @PostMapping("/darkroom/{filmId}")
    public void darkRoom(@PathVariable String characterName, @PathVariable Long filmId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.darkRoom(character, filmId);
    }

    @PostMapping("/combine/{itemAId}/{itemBId}")
    public void combineItems(@PathVariable String characterName, @PathVariable Long itemAId, @PathVariable Long itemBId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.combineItems(character, itemAId, itemBId);
    }

}