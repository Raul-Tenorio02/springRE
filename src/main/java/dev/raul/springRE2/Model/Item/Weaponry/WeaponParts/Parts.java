package dev.raul.springRE2.Model.Item.Weaponry.WeaponParts;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PART")
@NoArgsConstructor
public class Parts extends Item {

    @Enumerated(EnumType.STRING)
    private PartsCategory partsCategory;

    public Parts(Long id, String name, String description, ItemCategory itemCategory, String iconPath ,PartsCategory partsCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.partsCategory = partsCategory;
    }

    public PartsCategory getPartsCategory() {
        return partsCategory;
    }

    public void setPartsCategory(PartsCategory partsCategory) {
        this.partsCategory = partsCategory;
    }

}
