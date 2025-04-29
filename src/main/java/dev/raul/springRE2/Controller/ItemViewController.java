package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/index/items")
public class ItemViewController {

    private final ItemService itemService;

    public ItemViewController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String listItems(Model model) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/{id}")
    public String getItemDetails(@PathVariable Long id, Model model) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            model.addAttribute("item", item.get());
            return "item-details";
        } else {
            //TODO: add error handling
            return "error";
        }
    }

    @GetMapping("/category/{category}")
    public String getItemsByCategory(@PathVariable ItemCategory category, Model model) {
        List<Item> items = itemService.findByItemCategory(category);
        model.addAttribute("items", items);
        model.addAttribute("category", category);
        return "items-by-category";
    }

}
