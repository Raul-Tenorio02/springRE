package dev.raul.springRE2.Controller.CharactersControllers;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Service.InventoryServices.ClaireInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claire/inventory")
public class ClaireInventoryController {

    private final ClaireInventoryService claireInventoryService;

    public ClaireInventoryController(ClaireInventoryService claireInventoryService) {
        this.claireInventoryService = claireInventoryService;
    }

    @GetMapping
    public List<Item> getInventory() {
        return claireInventoryService.getInventory();
    }

    @GetMapping("/itembox")
    public List<Item> getItemBox() {
        return claireInventoryService.getItemBox();
    }

    @PostMapping("/collect/{itemId}")
    public void collectItem(@PathVariable Long itemId) {
        claireInventoryService.collectItem(itemId);
    }

    @PostMapping("/transfer/{itemId}")
    public void transferItem(@PathVariable Long itemId, @RequestParam boolean toItemBox) {
        claireInventoryService.itemBoxTransfer(itemId, toItemBox);
    }

    @PostMapping("/use/{weaponId}")
    public void useWeapon(@PathVariable Long weaponId) {
        claireInventoryService.useWeapon(weaponId);
    }

    @GetMapping("/file/{fileId}")
    public String readFile(@PathVariable Long fileId) {
        return claireInventoryService.readFile(fileId);
    }

    @PostMapping("/darkroom/{filmId}")
    public void darkRoom(@PathVariable Long filmId) {
        claireInventoryService.darkRoom(filmId);
    }

    @PostMapping("/combine/{itemAId}/{itemBId}")
    public void combineItems(@PathVariable Long itemAId, @PathVariable Long itemBId) {
        claireInventoryService.combineItems(itemAId, itemBId);
    }

}
