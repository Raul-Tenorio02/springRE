package dev.raul.springRE2.Model.Item.Recovery;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import dev.raul.springRE2.Service.ItemService;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@DiscriminatorValue("RECOVERY")
@NoArgsConstructor
public class RecoveryItem extends Item implements MixInterface {

    private final ItemService itemService;

    @Enumerated(EnumType.STRING)
    private RecoveryCategory recoveryCategory;

    public RecoveryItem(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, RecoveryCategory recoveryCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.recoveryCategory = recoveryCategory;
    }

    public RecoveryCategory getRecoveryCategory() {
        return recoveryCategory;
    }

    public void setRecoveryCategory(RecoveryCategory recoveryCategory) {
        this.recoveryCategory = recoveryCategory;
    }

    @Override
    public RecoveryItem mixHerb(RecoveryItem otherHerb) {
        if (this.getItemCategory() != ItemCategory.RECOVERY || otherHerb.getItemCategory() != ItemCategory.RECOVERY) {
            System.out.println("\nThere is no need of mixing these.");
            return null;
        }
        Map<Set<RecoveryCategory>, RecoveryItem> herbMixes = new HashMap<>();
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.GREEN)), itemService.findById().orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GG, RecoveryCategory.GREEN)), itemService.findById().orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.RED)), itemService.findById().orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.BLUE)), itemService.findById().orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GG, RecoveryCategory.BLUE)), itemService.findById().orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GR, RecoveryCategory.BLUE)), itemService.findById().orElse(null));

        Set<RecoveryCategory> herbTypes = new HashSet<>(Arrays.asList(this.recoveryCategory, otherHerb.recoveryCategory));

        RecoveryItem mixedHerb = herbMixes.get(herbTypes);
        if (mixedHerb == null) {
            System.out.println("\nThere is no need of mixing these.");
        }
        return mixedHerb;
    }

}
