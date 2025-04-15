package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Model.Inventories.ItemBox;
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

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeonInventoryService {

    private final ItemService itemService;
    private final InventoryService inventoryService;
    private final ItemBoxService itemBoxService;
    private final CharacterService characterService;
    private final WeaponService weaponService;
    private final RecoveryService recoveryService;
    private final KeyService keyService;

    private Character currentCharacter;

    public LeonInventoryService(ItemService itemService, InventoryService inventoryService, ItemBoxService itemBoxService, CharacterService characterService, WeaponService weaponService, RecoveryService recoveryService, KeyService keyService) {
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.itemBoxService = itemBoxService;
        this.characterService = characterService;
        this.weaponService = weaponService;
        this.recoveryService = recoveryService;
        this.keyService = keyService;
        initializeInventory();
    }

    private void initializeInventory() {
        this.currentCharacter = characterService.getCharacterById(1)
                .orElseThrow(() -> new RuntimeException("Character not found."));
        if (inventoryService.getInventory().isEmpty()) {
            addItemToInventory(30L);
            addItemToInventory(2L);
            addItemToInventory(1L);
        }
    }

    private void addItemToInventory(Long itemId) {
        itemService.findById(itemId)
                .ifPresent(item -> {
                    Inventory inventoryItem = new Inventory();
                    inventoryItem.setCharacters(currentCharacter);
                    inventoryItem.setItem(item);
                    inventoryService.setInventory(inventoryItem);
                });
    }

    private void removeItemFromInventory(Long itemId) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem().getId().equals(itemId))
                .findFirst()
                .ifPresent(inventoryItem -> inventoryService.deleteFromInventory(inventoryItem.getId()));
    }

    public void collectItem(Long itemId) {
        Optional<Item> item = itemService.findById(itemId);
        item.ifPresent(this::processItem);
    }

    private void processItem(Item item) {
        List<Inventory> currentInventory = inventoryService.getInventoryByCharacter(currentCharacter);
        if (currentInventory.size() < 8) {
            Optional<Inventory> existingInventoryItem = currentInventory.stream()
                    .filter(inv -> inv.getItem().getId().equals(item.getId()))
                    .findFirst();
            if (existingInventoryItem.isPresent()) {
                switch (item) {
                    case Ammo ammo -> stackAmmo(ammo);
                    case KeyItem keyItem -> stackInkRibbon(keyItem);
                    case File file -> readFile(file.getId());
                    default -> addItemToInventory(item.getId());
                }
            }
        } else {
            System.out.println("You cannot carry any more items.");
        }
    }

    private void stackAmmo(Ammo ammo) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof Ammo && inv.getItem().getName().equals(ammo.getName()))
                .findFirst()
                .ifPresent(inventoryItem -> {
                    Ammo existingAmmo = (Ammo) inventoryItem.getItem();
                    int quantityToAdd = switch (ammo.getAmmoCategory()) {
                        case HANDGUN_BULLETS -> 15;
                        case SHOTGUN_SHELLS -> 7;
                        case BOWGUN_BOLTS -> 18;
                        case GRENADE_ROUNDS, ACID_ROUNDS, FLAME_ROUNDS -> 6;
                        case MAGNUM_BULLETS -> 8;
                        case MACHINEGUN_BULLETS -> 100;
                    };
                    existingAmmo.setAmmoQuantity(existingAmmo.getAmmoQuantity() + quantityToAdd);
                    inventoryItem.setItem(existingAmmo);
                    inventoryService.setInventory(inventoryItem);
                });
    }

    private void stackInkRibbon(KeyItem keyItem) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof KeyItem && ((KeyItem) inv.getItem()).getKeyCategory() == KeyCategory.INK_RIBBON)
                .findFirst()
                .ifPresent(inventoryItem -> {
                    KeyItem existingKeyItem = (KeyItem) inventoryItem.getItem();
                    int quantityToAdd = switch (keyItem.getName()) {
                        case "Ink Ribbon" -> 3;
                        case "Small key" -> 1;
                        default -> throw new IllegalStateException("Unexpected value.");
                    };
                    existingKeyItem.setKeyQuantity(existingKeyItem.getKeyQuantity() + quantityToAdd);
                    inventoryItem.setItem(existingKeyItem);
                    inventoryService.setInventory(inventoryItem);
                });
    }

    public void updateInventoryAmmo(Ammo returnedAmmo) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof Ammo && ((Ammo) inv.getItem()).getAmmoCategory() == returnedAmmo.getAmmoCategory())
                .findFirst()
                .ifPresent(inventoryItem -> {
                    Ammo inventoryAmmo = (Ammo) inventoryItem.getItem();
                    inventoryAmmo.setAmmoQuantity(inventoryAmmo.getAmmoQuantity() + returnedAmmo.getAmmoQuantity());
                    inventoryItem.setItem(inventoryAmmo);
                    inventoryService.setInventory(inventoryItem);
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
            removeItemFromInventory(herb1.getId());
            removeItemFromInventory(herb2.getId());
            addItemToInventory(combinedHerb.getId());
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
                removeItemFromInventory(ammo.getId());
            }
        }
    }

    private void combineWeaponWithParts(Item item1, Item item2) {
        if (item1 instanceof Weapon && item2 instanceof Parts || item1 instanceof Parts && item2 instanceof Weapon) {
            Weapon weapon = (item1 instanceof Weapon) ? (Weapon) item1 : (Weapon) item2;
            Parts parts = (item1 instanceof Parts) ? (Parts) item1 : (Parts) item2;
            Weapon customWeapon = weaponService.upgradeWeapon(weapon, parts);
            if (customWeapon != null) {
                removeItemFromInventory(parts.getId());
                removeItemFromInventory(weapon.getId());
                addItemToInventory(customWeapon.getId());
            }
        }
    }

    private void craftKeyItem(KeyItem item1, KeyItem item2) {
        KeyItem combinedKeyItem = keyService.combineKeyItems(item1, item2);
        if (combinedKeyItem != null) {
            removeItemFromInventory(item1.getId());
            removeItemFromInventory(item2.getId());
            addItemToInventory(combinedKeyItem.getId());
        }
    }

    public void itemBoxTransfer(Long itemId, boolean toItemBox) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem().getId().equals(itemId))
                .findFirst()
                .ifPresent(inventoryItem -> {
                    if (toItemBox){
                        ItemBox itemBoxItem = new ItemBox();
                        itemBoxItem.setCharacters(currentCharacter);
                        itemBoxItem.setItem(inventoryItem.getItem());
                        itemBoxItem.setItem(inventoryItem.getItem());
                        itemBoxService.addToItemBox(itemBoxItem);
                        inventoryService.deleteFromInventory(inventoryItem.getId());
                    } else {
                        addItemToInventory(inventoryItem.getItem().getId());
                        itemBoxService.deleteFromItemBox(inventoryItem.getId());
                    }
                });
    }

    public void useWeapon(Long weaponId) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof Weapon && inv.getItem().getId().equals(weaponId))
                .findFirst()
                .ifPresent(inventoryWeapon -> weaponService.weaponUseCount((Weapon) inventoryWeapon.getItem(), 1));
    }

    public String readFile(Long fileId) {
        return inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof File && inv.getItem().getId().equals(fileId))
                .findFirst()
                .map(inventoryFile -> inventoryFile.getItem().getDescription())
                .orElse(null);
    }

    public void darkRoom(Long filmId) {
        inventoryService.getInventoryByCharacter(currentCharacter).stream()
                .filter(inv -> inv.getItem() instanceof KeyItem && inv.getItem().getId().equals(filmId) && ((KeyItem) inv.getItem()).getKeyCategory() == KeyCategory.FILM)
                .findFirst()
                .ifPresent(inventoryFilm -> {
                    KeyItem film = (KeyItem) inventoryFilm.getItem();
                    File developedFile = keyService.developFilm(film);
                    if (developedFile != null) {
                        inventoryService.deleteFromInventory(inventoryFilm.getId());
                        addItemToInventory(developedFile.getId());
                        readFile(developedFile.getId());
                    }
                });
    }
}
