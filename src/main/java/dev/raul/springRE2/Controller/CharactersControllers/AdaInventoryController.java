package dev.raul.springRE2.Controller.CharactersControllers;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Service.InventoryServices.AdaInventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class AdaInventoryController {

    private final AdaInventoryService adaInventoryService;

    public AdaInventoryController(AdaInventoryService adaInventoryService) {
        this.adaInventoryService = adaInventoryService;
    }

    @GetMapping
    public List<Item> getInventory() {
        return adaInventoryService.getInventory();
    }

    @GetMapping("/itembox")
    public List<Item> getItemBox() {
        return adaInventoryService.getItemBox();
    }

    @PostMapping("/collect/{itemId}")
    public void collectItem(@PathVariable Long itemId) {
        adaInventoryService.collectItem(itemId);
    }

    @PostMapping("/transfer/{itemId}")
    public void transferItem(@PathVariable Long itemId, @RequestParam boolean toItemBox) {
        adaInventoryService.itemBoxTransfer(itemId, toItemBox);
    }

    @PostMapping("/use/{weaponId}")
    public void useWeapon(@PathVariable Long weaponId) {
        adaInventoryService.useWeapon(weaponId);
    }

    @GetMapping("/file/{fileId}")
    public String readFile(@PathVariable Long fileId) {
        return adaInventoryService.readFile(fileId);
    }

    @PostMapping("/darkroom/{filmId}")
    public void darkRoom(@PathVariable Long filmId) {
        adaInventoryService.darkRoom(filmId);
    }

    @PostMapping("/combine/{itemAId}/{itemBId}")
    public void combineItems(@PathVariable Long itemAId, @PathVariable Long itemBId) {
        adaInventoryService.combineItems(itemAId, itemBId);
    }

}
