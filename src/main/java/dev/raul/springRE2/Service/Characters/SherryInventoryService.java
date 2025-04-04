package dev.raul.springRE2.Service.Characters;

import dev.raul.springRE2.Model.Item.Files.File;
import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.Key.KeyCategory;
import dev.raul.springRE2.Model.Item.Key.KeyItem;
import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Item.Weaponry.Weapons.Weapon;
import dev.raul.springRE2.Service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SherryInventoryService {

    private final ItemService itemService;
    private final List<Item> inventory;
    private final List<Item> itemBox;

    public SherryInventoryService(ItemService itemService) {
        this.itemService = itemService;
        this.inventory = new ArrayList<>();
        this.itemBox = new ArrayList<>();
        initializeInventory();
    }

    private void initializeInventory() {
        if (inventory.isEmpty()) {
            inventory.add(itemService.findById().orElse(null));
            inventory.add(itemService.findById().orElse(null));
            inventory.add(itemService.findById().orElse(null));
        }
    }

    public void collectItem(Long itemId) {
        Optional<Item> item = itemService.findById(itemId);
        item.ifPresent(this::processItem);
    }

    private void processItem(Item item) {
        switch (item) {
            case Ammo ammo -> stackAmmo(ammo);
            case KeyItem keyItem -> stackInkRibbon(keyItem);
            //case File file -> readFile(file.getId()); //TODO: create archive service
            default -> inventory.add(item);
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

    public void updateInventoryAmmo(Ammo returnedAmmo) {
        inventory.stream()
                .filter(item -> item instanceof Ammo && ((Ammo) item).getAmmoCategory() == returnedAmmo.getAmmoCategory())
                .peek(item -> ((Ammo) item).setAmmoQuantity(((Ammo) item).getAmmoQuantity() + returnedAmmo.getAmmoQuantity()))
                .findFirst()
                .orElseGet(() -> {
                    inventory.add(returnedAmmo);
                    return returnedAmmo;
                });
    }

    public void itemBoxTransfer(Long itemId, boolean toItemBox) {
        if (toItemBox) {
            // i instantiate Item
            Item item = inventory.stream().filter(i -> i.getId().equals(itemId))
                    .findFirst()
                    .orElse(null);
            if (item != null) {
                itemBox.add(item);
                inventory.remove(item);
            }
        } else {
            Item item = itemBox.stream().filter(i -> i.getId().equals(itemId))
                    .findFirst()
                    .orElse(null);
            if (item != null) {
                inventory.add(item);
                itemBox.remove(item);
            }
        }
    }

    public void useWeapon(Long weaponId) {
        Weapon weapon = (Weapon) inventory.stream()
                .filter(i -> i instanceof Weapon && i.getId().equals(weaponId))
                .findFirst()
                .orElse(null);
        if (weapon != null) {
            weapon.weaponUseCount(1);
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

    public void darkRoom(Long filmId) {
        KeyItem film = (KeyItem) inventory.stream().filter(i -> i instanceof KeyItem && i.getId().equals(filmId))
                .findFirst().
                orElse(null);
        if (film != null && film.getKeyCategory() == KeyCategory.FILM) {
            File developedFile = film.developFilm(film);
            if (developedFile != null) {
                inventory.remove(film);
                inventory.add(developedFile);
                readFile(developedFile.getId());
            }
        }
    }
}
