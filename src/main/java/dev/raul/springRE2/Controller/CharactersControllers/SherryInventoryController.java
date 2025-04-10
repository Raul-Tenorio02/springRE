package dev.raul.springRE2.Controller.CharactersControllers;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Service.InventoryServices.SherryInventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public class SherryInventoryController {

    private final SherryInventoryService sherryInventoryService;

    public SherryInventoryController(SherryInventoryService sherryInventoryService) {
        this.sherryInventoryService = sherryInventoryService;
    }

    @GetMapping
    public List<Item> getInventory() {
        return sherryInventoryService.getInventory();
    }

    @PostMapping("/collect/{itemId}")
    public void collectItem(@PathVariable Long itemId) {
        sherryInventoryService.collectItem(itemId);
    }

    @GetMapping("/file/{fileId}")
    public String readFile(@PathVariable Long fileId) {
        return sherryInventoryService.readFile(fileId);
    }

    @PostMapping("/combine/{itemAId}/{itemBId}")
    public void combineItems(@PathVariable Long itemAId, @PathVariable Long itemBId) {
        sherryInventoryService.combineItems(itemAId, itemBId);
    }

}
