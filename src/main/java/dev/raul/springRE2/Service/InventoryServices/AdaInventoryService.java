package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Items.Files.File;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.Key.KeyCategory;
import dev.raul.springRE2.Model.Items.Key.KeyItem;
import dev.raul.springRE2.Model.Items.Recovery.RecoveryItem;
import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Items.Weaponry.WeaponParts.Parts;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.ReloadRecord;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.Weapon;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.WeaponCategory;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import dev.raul.springRE2.Service.ItemsServices.Key.KeyService;
import dev.raul.springRE2.Service.ItemsServices.Recovery.RecoveryService;
import dev.raul.springRE2.Service.ItemsServices.Weaponry.WeaponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdaInventoryService {

    private final ItemService itemService;
    private final WeaponService weaponService;
    private final RecoveryService recoveryService;
    private final KeyService keyService;
    private final List<Item> inventory;
    private final List<Item> itemBox;

    public List<Item> getInventory() {
        return inventory;
    }

    public List<Item> getItemBox() {
        return itemBox;
    }

    public AdaInventoryService(ItemService itemService, WeaponService weaponService, RecoveryService recoveryService, KeyService keyService) {
        this.itemService = itemService;
        this.weaponService = weaponService;
        this.recoveryService = recoveryService;
        this.keyService = keyService;
        this.inventory = new ArrayList<>();
        this.itemBox = new ArrayList<>();
        initializeInventory();
    }

    private void initializeInventory() {
        if (inventory.isEmpty()) {
            Ammo handgunBullets = (Ammo) itemService.findById(22L).orElse(null);
            if (handgunBullets != null){
                handgunBullets.setAmmoQuantity(45);
            }
            inventory.add(itemService.findById(32L).orElse(null));
            inventory.add(itemService.findById(4L).orElse(null));
            inventory.add(handgunBullets);
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

    public void combineItems(Long itemA, Long itemB) {
        Item item1 = itemService.findById(itemA).orElse(null);
        Item item2 = itemService.findById(itemB).orElse(null);

        if (item1 instanceof RecoveryItem herb1 && item2 instanceof RecoveryItem herb2) {
            combineHerbs(herb1, herb2);
        } else if (item1 instanceof Weapon && item2 instanceof Ammo || item1 instanceof Ammo && item2 instanceof Weapon) {
            combineWeaponWithAmmo(item1, item2);
        } else if (item1 instanceof Weapon && item2 instanceof Parts || item1 instanceof Parts && item2 instanceof Weapon) {
            combineWeaponWithParts(item1, item2);
        } else if (item1 instanceof KeyItem keyItem1 && item2 instanceof KeyItem keyItem2) {
            craftKeyItem(keyItem1, keyItem2);
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

    private void combineWeaponWithAmmo(Item item1, Item item2) {
        if (item1 instanceof Weapon && item2 instanceof Ammo || item1 instanceof Ammo && item2 instanceof Weapon) {
            Weapon weapon = (item1 instanceof Weapon) ? (Weapon) item1 : (Weapon) item2;
            Ammo ammo = (item1 instanceof Ammo) ? (Ammo) item1 : (Ammo) item2;
            ReloadRecord reloadResult = weaponService.reloadWeapon(weapon, ammo);
            Ammo returnedAmmo = reloadResult.returnedAmmo();
            if (returnedAmmo != null) {
                updateInventoryAmmo(returnedAmmo);
            }
            if (weapon.getWeaponCategory() != WeaponCategory.INFINITE_WEAPON && ammo.getAmmoQuantity() <= 0) {
                inventory.remove(ammo);
            }
        }
    }

    private void combineWeaponWithParts(Item item1, Item item2) {
        if (item1 instanceof Weapon && item2 instanceof Parts || item1 instanceof Parts && item2 instanceof Weapon) {
            Weapon weapon = (item1 instanceof Weapon) ? (Weapon) item1 : (Weapon) item2;
            Parts parts = (item1 instanceof Parts) ? (Parts) item1 : (Parts) item2;
            Weapon customWeapon = weaponService.upgradeWeapon(weapon, parts);
            if (customWeapon != null) {
                inventory.remove(parts);
                inventory.remove(weapon);
                inventory.add(customWeapon);
            }
        }
    }

    private void craftKeyItem(KeyItem item1, KeyItem item2) {
        KeyItem combinedKeyItem = keyService.combineKeyItems(item1, item2);
        if (combinedKeyItem != null) {
            inventory.remove(item1);
            inventory.remove(item2);
            inventory.add(combinedKeyItem);
        }
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
            weaponService.weaponUseCount(weapon, 1);
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
            File developedFile = keyService.developFilm(film);
            if (developedFile != null) {
                inventory.remove(film);
                inventory.add(developedFile);
                readFile(developedFile.getId());
            }
        }
    }

}
