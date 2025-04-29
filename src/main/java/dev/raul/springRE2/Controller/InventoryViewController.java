package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Model.Inventories.ItemBox;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Service.InventoryServices.CharacterInventoryService;
import dev.raul.springRE2.Service.InventoryServices.CharacterService;
import dev.raul.springRE2.Service.InventoryServices.ItemBoxService;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryViewController {

    private final CharacterInventoryService characterInventoryService;
    private final ItemBoxService itemBoxService;
    private final ItemService itemService;
    private final CharacterService characterService;

    public InventoryViewController(CharacterInventoryService characterInventoryService, ItemBoxService itemBoxService, ItemService itemService, CharacterService characterService) {
        this.characterInventoryService = characterInventoryService;
        this.itemBoxService = itemBoxService;
        this.itemService = itemService;
        this.characterService = characterService;
    }

    @GetMapping("/{characterName}")
    public String getInventory(@PathVariable String characterName, Model model) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        List<Inventory> inventory = characterInventoryService.getInventoryByCharacter(character);
        model.addAttribute("characterName", characterName);
        model.addAttribute("inventory", inventory);
        return "inventory";
    }

    @GetMapping("/{characterName}/itembox")
    public String getItemBox(@PathVariable String characterName, Model model) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        List<ItemBox> itemBox = itemBoxService.getItemBoxByCharacter(character);
        model.addAttribute("characterName", characterName);
        model.addAttribute("itemBox", itemBox);
        return "item-box";
    }

    @GetMapping("/{characterName}/collect")
    public String showCollectItemList(@PathVariable String characterName,
                                      @RequestParam(value = "filterType", required = false, defaultValue = "all") String filterType,
                                      @RequestParam(value = "filterValue", required = false) String filterValue, Model model){
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        List<Item> itemList = new ArrayList<>();

        switch (filterType) {
            case "id":
                if (filterValue != null && !filterValue.isEmpty()) {
                    try {
                        Optional<Item> item = itemService.findById(Long.parseLong(filterValue));
                        item.ifPresent(itemList::add);
                    } catch (NumberFormatException e) {
                        // error handling for IDs that doesn't exist
                        model.addAttribute("errorMessage", "Invalid ID");
                        itemList = itemService.findAllItems();
                    }
                } else {
                    itemList = itemService.findAllItems();
                }
                break;
            case "category":
                if (filterValue != null && !filterValue.isEmpty()) {
                    try {
                        ItemCategory category = ItemCategory.valueOf(filterValue.toUpperCase());
                        itemList = itemService.findByItemCategory(category);
                    } catch (IllegalArgumentException e) {
                        model.addAttribute("errorMessage", "Invalid Category.");
                        itemList = itemService.findAllItems();
                    }
                } else {
                    itemList = itemService.findAllItems();
                }
                break;
            case "all":
            default:
                itemList = itemService.findAllItems();
                break;
        }

        model.addAttribute("characterName", characterName);
        model.addAttribute("allItems", itemList);
        model.addAttribute("filterType", filterType);
        model.addAttribute("filterValue", filterValue);
        model.addAttribute("categories", ItemCategory.values());
        return "collect-item";
    }

    @PostMapping("/{characterName}/collect/{itemId}")
    public String collectItemFromList(@PathVariable String characterName, @PathVariable Long itemId) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        characterInventoryService.collectItem(character, itemId);
        return "redirect:/inventory/" + characterName;
    }

    //TODO: add transfer, use, etc.

    @GetMapping("/{characterName}/file/{fileId}")
    public String readFile(@PathVariable String characterName, @PathVariable Long fileId, Model model) {
        Character character = characterService.findCharacterByName(characterName)
                .orElseThrow(() -> new RuntimeException("Character not found: " + characterName));
        String fileContent = characterInventoryService.readFile(character, fileId);
        model.addAttribute("characterName", characterName);
        model.addAttribute("fileContent", fileContent);
        return "file-content";
    }

}
