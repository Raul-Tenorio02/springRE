package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import dev.raul.springRE2.Service.ItemService;
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

    //GET
    @GetMapping
    public List<Item> getAll(){return itemService.getAll();}
    //GET BY ID
    @GetMapping("/{id}")
    public Optional<Item> getById(@PathVariable Long id){
        return itemService.findById(id);
    }

    //GET BY CATEGORY
    @GetMapping("/category/{category}")
    public List<Item> getByCategory(@PathVariable ItemCategory category) {
        return itemService.findByItemCategory(category);
    }

    //POST
    @PostMapping
    public Item create(@RequestBody Item item){return itemService.save(item);}

    //DELETE
    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id){itemService.delete(id);}

}
