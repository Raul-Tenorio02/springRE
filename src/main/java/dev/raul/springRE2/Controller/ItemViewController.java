package dev.raul.springRE2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemViewController {

    /*private final ItemService itemService;

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
    }*/
}
