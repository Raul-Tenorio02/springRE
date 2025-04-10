package dev.raul.springRE2.Controller.CharactersControllers;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Service.InventoryServices.LeonInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leon/inventory")
public class LeonInventoryController {

    private final LeonInventoryService leonInventoryService;

    public LeonInventoryController(LeonInventoryService leonInventoryService) {
        this.leonInventoryService = leonInventoryService;
    }

    @GetMapping
    public List<Item> getInventory() {
        return leonInventoryService.getInventory();
    }

    @GetMapping("/itembox")
    public List<Item> getItemBox() {
        return leonInventoryService.getItemBox();
    }

    @PostMapping("/collect/{itemId}")
    public void collectItem(@PathVariable Long itemId) {
        leonInventoryService.collectItem(itemId);
    }

    @PostMapping("/transfer/{itemId}")
    public void transferItem(@PathVariable Long itemId, @RequestParam boolean toItemBox) {
        leonInventoryService.itemBoxTransfer(itemId, toItemBox);
    }

    @PostMapping("/use/{weaponId}")
    public void useWeapon(@PathVariable Long weaponId) {
        leonInventoryService.useWeapon(weaponId);
    }

    @GetMapping("/file/{fileId}")
    public String readFile(@PathVariable Long fileId) {
        return leonInventoryService.readFile(fileId);
    }

    @PostMapping("/darkroom/{filmId}")
    public void darkRoom(@PathVariable Long filmId) {
        leonInventoryService.darkRoom(filmId);
    }

    @PostMapping("/combine/{itemAId}/{itemBId}")
    public void combineItems(@PathVariable Long itemAId, @PathVariable Long itemBId) {
        leonInventoryService.combineItems(itemAId, itemBId);
    }

}
