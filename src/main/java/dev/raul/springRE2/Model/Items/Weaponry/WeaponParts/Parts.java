package dev.raul.springRE2.Model.Items.Weaponry.WeaponParts;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PART")
public class Parts extends Item {

    @Enumerated(EnumType.STRING)
    private PartsCategory partsCategory;

    public Parts() {}

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
