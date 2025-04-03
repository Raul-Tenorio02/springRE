package dev.raul.springRE2.Model.Item.Key;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "key_tb")
@NoArgsConstructor
public class KeyItem extends Item {

    private final KeyCategory keyCategory;
    private int quantity;

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, KeyCategory keyCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.keyCategory = keyCategory;
    }

    public KeyItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, KeyCategory keyCategory, int quantity) {
        super(id, name, description, itemCategory, iconPath);
        this.keyCategory = keyCategory;
        this.quantity = quantity;
    }

    public KeyCategory getKeyCategory() {
        return keyCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
