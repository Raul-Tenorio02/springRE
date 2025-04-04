package dev.raul.springRE2.Model.Item;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_tb")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    private String iconPath;

    public Item(Long id, String name, String description, ItemCategory itemCategory, String iconPath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemCategory = itemCategory;
        this.iconPath = iconPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

}
