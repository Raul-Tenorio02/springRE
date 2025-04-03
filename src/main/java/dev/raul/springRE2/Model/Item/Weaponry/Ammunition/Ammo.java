package dev.raul.springRE2.Model.Item.Weaponry.Ammunition;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ammo_tb")
@NoArgsConstructor
public class Ammo extends Item {

    private int quantity;

    private AmmoCategory ammoCategory;

    public Ammo(Long id, String name, String description, ItemCategory itemCategory, String iconPath, AmmoCategory ammoCategory, int quantity) {
        super(id, name, description, itemCategory, iconPath);
        this.ammoCategory = ammoCategory;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int updateQuantity(int quantity, int n) {
        return quantity * n;
    }

    public AmmoCategory getAmmoCategory() {
        return ammoCategory;
    }

    public void setAmmoCategory(AmmoCategory ammoCategory) {
        this.ammoCategory = ammoCategory;
    }
}
