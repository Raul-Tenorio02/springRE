package dev.raul.springRE2.Model.Item.Recovery;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recovery_tb")
@NoArgsConstructor
public class RecoveryItem extends Item {

    private final RecoveryCategory recoveryCategory;

    public RecoveryItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, RecoveryCategory recoveryCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.recoveryCategory = recoveryCategory;
    }

}
