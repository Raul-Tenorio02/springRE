package dev.raul.springRE2.Model.Items.Files;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FILE")
@NoArgsConstructor
@Data
public class File extends Item {

    public File(Long id, String name, String description, ItemCategory itemCategory, String iconPath) {
        super(id, name, description, itemCategory, iconPath);
    }

}
