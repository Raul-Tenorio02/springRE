package dev.raul.springRE2.Model.Item.Key;

import dev.raul.springRE2.Model.Item.Files.File;
import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import dev.raul.springRE2.Service.ItemService;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Entity
@DiscriminatorValue("KEY")
@NoArgsConstructor
public class KeyItem extends Item implements KeyInterface {

    private final ItemService itemService;

    @Enumerated(EnumType.STRING)
    private KeyCategory keyCategory;

    private Integer keyQuantity;

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, KeyCategory keyCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.keyCategory = keyCategory;
    }

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, KeyCategory keyCategory, Integer keyQuantity) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.keyCategory = keyCategory;
        this.keyQuantity = keyQuantity;
    }

    public KeyCategory getKeyCategory() {
        return keyCategory;
    }

    public void setKeyCategory(KeyCategory keyCategory) {
        this.keyCategory = keyCategory;
    }

    public Integer getKeyQuantity() {
        return keyQuantity;
    }

    public void setKeyQuantity(Integer keyQuantity) {
        this.keyQuantity = keyQuantity;
    }

    @Override
    public File developFilm(KeyItem film) {
        if (this.keyCategory == KeyCategory.FILM) {
            switch (film.getId()) {
                case 38: return itemService.findById().orElse(null);
                case 39: return itemService.findById().orElse(null);
                case 40: return itemService.findById().orElse(null);
                case 41: return itemService.findById().orElse(null);
            }
        }
        return null;
    }

    @Override
    public KeyItem combineKeyItems(KeyItem otherKeyItem){
        if (this.getItemCategory() != ItemCategory.KEY && otherKeyItem.getItemCategory() != ItemCategory.KEY) {
            return null;
        }

        Map<Set<Long>, KeyItem> keyCrafts = Map.of(
                Set.of(itemService.findById().orElse(), itemService.findById().orElse()), itemService.findById().orElse(),
                Set.of(itemService.findById().orElse(), itemService.findById().orElse()), itemService.findById().orElse()
        );
        Set<Long> keySet = Set.of(this.getId(), otherKeyItem.getId());

        return keyCrafts.getOrDefault(keySet, null);
    }

}
