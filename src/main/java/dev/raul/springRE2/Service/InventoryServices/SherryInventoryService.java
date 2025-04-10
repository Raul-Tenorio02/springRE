package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Items.Files.File;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.Key.KeyCategory;
import dev.raul.springRE2.Model.Items.Key.KeyItem;
import dev.raul.springRE2.Model.Items.Recovery.RecoveryItem;
import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import dev.raul.springRE2.Service.ItemsServices.Recovery.RecoveryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SherryInventoryService {

    private final ItemService itemService;
    private final RecoveryService recoveryService;
    private final List<Item> inventory;

    public List<Item> getInventory() {
        return inventory;
    }

    public SherryInventoryService(ItemService itemService, RecoveryService recoveryService) {
        this.itemService = itemService;
        this.recoveryService = recoveryService;
        this.inventory = new ArrayList<>();
        initializeInventory();
    }

    private void initializeInventory() {
        if (inventory.isEmpty()) {
            inventory.add(itemService.findById(33L).orElse(null));
            inventory.add(itemService.findById(88L).orElse(null));
        }
    }

    public void collectItem(Long itemId) {
        Optional<Item> item = itemService.findById(itemId);
        item.ifPresent(this::processItem);
    }

    private void processItem(Item item) {
        if (inventory.size() < 8) {
            switch (item) {
                case Ammo ammo -> stackAmmo(ammo);
                case KeyItem keyItem -> stackInkRibbon(keyItem);
                case File file -> readFile(file.getId());
                default -> inventory.add(item);
            }
        } else {
            System.out.println("You cannot carry any more items.");
        }
    }

    private void stackAmmo(Ammo ammo) {
        inventory.stream()
                .filter(inventoryItem -> inventoryItem instanceof Ammo && inventoryItem.getName().equals(ammo.getName()))
                .findFirst()
                .ifPresentOrElse(inventoryAmmo -> ammoToStack((Ammo) inventoryAmmo, ammo), () -> inventory.add(ammo));
    }

    private void ammoToStack(Ammo inventoryAmmo, Ammo ammo) {
        int quantityToAdd = switch (ammo.getAmmoCategory()) {
            case HANDGUN_BULLETS -> 15;
            case SHOTGUN_SHELLS -> 7;
            case BOWGUN_BOLTS -> 18;
            case GRENADE_ROUNDS, ACID_ROUNDS, FLAME_ROUNDS -> 6;
            case MAGNUM_BULLETS -> 8;
            case MACHINEGUN_BULLETS -> 100;
        };
        inventoryAmmo.setAmmoQuantity(inventoryAmmo.getAmmoQuantity() + quantityToAdd);
    }

    private void stackInkRibbon(KeyItem keyItem) {
        Optional<Item> inkRibbon = inventory.stream()
                .filter(item -> item instanceof KeyItem && ((KeyItem) item).getKeyCategory() == KeyCategory.INK_RIBBON)
                .findFirst();

        if (inkRibbon.isPresent()) {
            KeyItem inkToStack = (KeyItem) inkRibbon.get();
            inkToStack.setKeyQuantity(inkToStack.getKeyQuantity() + 3);
        } else {
            inventory.add(keyItem);
        }
    }

    public void combineItems(Long itemA, Long itemB) {
        Item item1 = itemService.findById(itemA).orElse(null);
        Item item2 = itemService.findById(itemB).orElse(null);

        if (item1 instanceof RecoveryItem herb1 && item2 instanceof RecoveryItem herb2) {
            combineHerbs(herb1, herb2);
        } else {
            System.out.println("\nYou cannot combine these items.");
        }
    }

    private void combineHerbs(RecoveryItem herb1, RecoveryItem herb2) {
        RecoveryItem combinedHerb = recoveryService.mixHerb(herb1, herb2);
        if (combinedHerb != null){
            inventory.remove(herb1);
            inventory.remove(herb2);
            inventory.add(combinedHerb);
        }
    }

    public String readFile(Long fileId) {
        File file = (File) itemService.findById(fileId)
                .orElse(null);
        if (file != null) {
            return file.getDescription();
        }
        return null;
    }

}
