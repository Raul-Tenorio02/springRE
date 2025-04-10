package dev.raul.springRE2.Model.Items.Recovery;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("RECOVERY")
@NoArgsConstructor
public class RecoveryItem extends Item {

    @Enumerated(EnumType.STRING)
    private RecoveryCategory recoveryCategory;

    public RecoveryItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, RecoveryCategory recoveryCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.recoveryCategory = recoveryCategory;
    }

    public RecoveryCategory getRecoveryCategory() {
        return recoveryCategory;
    }

    public void setRecoveryCategory(RecoveryCategory recoveryCategory) {
        this.recoveryCategory = recoveryCategory;
    }

}
