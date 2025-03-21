package dev.raul.springRE2.Controller;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/items")
public class ItemViewController {

    private final ItemService itemService;

    public ItemViewController(ItemService itemService) {
        this.itemService = itemService;
    }

    //READ
    @GetMapping
    public String listItems(Model model) {
        List<Item> items = itemService.getAll();
        model.addAttribute("items", items != null ? items : new ArrayList<>());
        return "items";
    }

    //GET CREATE FORM
    @GetMapping("/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "new-item";
    }

    //CREATE
    @PostMapping
    public String createItem(@ModelAttribute Item item, @RequestParam("icon")MultipartFile iconFile) throws IOException {
        String uploadDir = "src/main/resources/static/images/";
        String fileName = UUID.randomUUID() + "-" + iconFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        Files.copy(iconFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        item.setIconPath("/images/" + fileName);

        itemService.save(item);
        return "redirect:/items";
    }

    //DELETE BY ID
    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return "redirect:/items";
    }
}
