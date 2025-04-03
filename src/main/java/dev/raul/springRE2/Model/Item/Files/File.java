package dev.raul.springRE2.Model.Item.Files;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;

public class File extends Item {

    public File(Long id, String name, String description, ItemCategory itemCategory, String iconPath) {
        super(id, name, description, itemCategory, iconPath);
    }

}
