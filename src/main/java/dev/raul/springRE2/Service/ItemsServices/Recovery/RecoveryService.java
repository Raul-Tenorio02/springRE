package dev.raul.springRE2.Service.ItemsServices.Recovery;

import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Model.Items.Recovery.RecoveryCategory;
import dev.raul.springRE2.Model.Items.Recovery.RecoveryItem;
import dev.raul.springRE2.Repository.RecoveryRepository;
import dev.raul.springRE2.Service.ItemsServices.ItemService;

import java.util.*;

public class RecoveryService implements RecoveryInterface {

    private final ItemService itemService;
    private final RecoveryRepository recoveryRepository;

    public RecoveryService(ItemService itemService, RecoveryRepository recoveryRepository) {
        this.itemService = itemService;
        this.recoveryRepository = recoveryRepository;
    }

    @Override
    public RecoveryItem mixHerb(RecoveryItem herbA, RecoveryItem herbB) {
        if (herbA.getItemCategory() != ItemCategory.RECOVERY || herbB.getItemCategory() != ItemCategory.RECOVERY) {
            System.out.println("\nThere is no need of mixing these.");
            return null;
        }
        Map<Set<RecoveryCategory>, RecoveryItem> herbMixes = new HashMap<>();
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.GREEN)), (RecoveryItem) itemService.findById(89L).orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.RED)), (RecoveryItem) itemService.findById(90L).orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GREEN, RecoveryCategory.BLUE)), (RecoveryItem) itemService.findById(91L).orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GG, RecoveryCategory.BLUE)), (RecoveryItem) itemService.findById(92L).orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GG, RecoveryCategory.GREEN)), (RecoveryItem) itemService.findById(93L).orElse(null));
        herbMixes.put(new HashSet<>(Arrays.asList(RecoveryCategory.GR, RecoveryCategory.BLUE)), (RecoveryItem) itemService.findById(94L).orElse(null));

        Set<RecoveryCategory> herbTypes = new HashSet<>(Arrays.asList(herbA.getRecoveryCategory(), herbA.getRecoveryCategory()));

        RecoveryItem mixedHerb = herbMixes.get(herbTypes);
        if (mixedHerb == null) {
            System.out.println("\nThere is no need of mixing these.");
        }
        return mixedHerb;
    }

}
