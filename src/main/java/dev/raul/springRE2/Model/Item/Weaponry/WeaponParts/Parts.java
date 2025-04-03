package dev.raul.springRE2.Model.Item.Weaponry.WeaponParts;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parts_tb")
@NoArgsConstructor
public class Parts extends Item {

    private final PartsCategory partsCategory;

    public Parts(Long id, String name, String description, ItemCategory itemCategory, String iconPath ,PartsCategory partsCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.partsCategory = partsCategory;
    }

    public PartsCategory getTypePart() {
        return partsCategory;
    }

}
