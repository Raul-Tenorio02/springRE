package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //Listing items in database
    @GetMapping
    public List<Item> getAll(){return itemService.findAllItems();}

    @GetMapping("/{id}")
    public Optional<Item> getById(@PathVariable Long id){
        return itemService.findById(id);
    }

    @GetMapping("/category/{category}")
    public List<Item> getByCategory(@PathVariable ItemCategory category) {
        return itemService.findByItemCategory(category);
    }

}
