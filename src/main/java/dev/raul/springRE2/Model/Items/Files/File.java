package dev.raul.springRE2.Model.Items.Files;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FILE")
public class File extends Item {

    public File() {}

    public File(Long id, String name, String description, ItemCategory itemCategory, String iconPath) {
        super(id, name, description, itemCategory, iconPath);
    }

}
