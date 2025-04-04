package dev.raul.springRE2.Model.Item.Weaponry.Ammunition;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("AMMUNITION")
@NoArgsConstructor
public class Ammo extends Item {

    private Integer ammoQuantity;

    @Enumerated(EnumType.STRING)
    private AmmoCategory ammoCategory;

    public Ammo(Long id, String name, String description, ItemCategory itemCategory, String iconPath, AmmoCategory ammoCategory, Integer ammoQuantity) {
        super(id, name, description, itemCategory, iconPath);
        this.ammoCategory = ammoCategory;
        this.ammoQuantity = ammoQuantity;
    }

    public Integer getAmmoQuantity() {
        return ammoQuantity;
    }

    public void setAmmoQuantity(Integer ammoQuantity) {
        this.ammoQuantity = ammoQuantity;
    }

    public int updateAmmoQuantity(int quantity, int n) {
        return quantity * n;
    }

    public AmmoCategory getAmmoCategory() {
        return ammoCategory;
    }

    public void setAmmoCategory(AmmoCategory ammoCategory) {
        this.ammoCategory = ammoCategory;
    }
}
