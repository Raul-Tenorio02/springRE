package dev.raul.springRE2.Model.Items.Key;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("KEY")
public class KeyItem extends Item {

    @Enumerated(EnumType.STRING)
    private KeyCategory keyCategory;

    private Integer keyQuantity;

    public KeyItem(){}

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, KeyCategory keyCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.keyCategory = keyCategory;
    }

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, KeyCategory keyCategory, Integer keyQuantity) {
        super(id, name, description, itemCategory, iconPath);
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

}
