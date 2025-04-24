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
import dev.raul.springRE2.Repository.InventoryRepository;
import dev.raul.springRE2.Repository.ItemRepository;
import dev.raul.springRE2.Service.ItemsServices.Key.KeyService;
import dev.raul.springRE2.Service.ItemsServices.Recovery.RecoveryService;
import dev.raul.springRE2.Service.ItemsServices.Weaponry.WeaponService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CharacterInventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final ItemBoxService itemBoxService;
    private final WeaponService weaponService;
    private final RecoveryService recoveryService;
    private final KeyService keyService;

    public CharacterInventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository, ItemBoxService itemBoxService, WeaponService weaponService, RecoveryService recoveryService, KeyService keyService) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.itemBoxService = itemBoxService;
        this.weaponService = weaponService;
        this.recoveryService = recoveryService;
        this.keyService = keyService;
    }

    public List<Inventory> getInventoryByCharacter(Character character) {
        return inventoryRepository.findByCharacter(character);
    }

    public void collectItem(Character character, Long itemId) {
        itemRepository.findById(itemId)
                .ifPresent(item -> {
                    List<Inventory> currentInventory = inventoryRepository.findByCharacter(character);
                    if (currentInventory.size() < 8) {
                        Optional<Inventory> existingInventoryItem = currentInventory.stream()
                                .filter(inventory -> inventory.getItem().getId().equals(item.getId()))
                                .findFirst();
                        if (existingInventoryItem.isPresent()) {
                            if (item instanceof Ammo ammo) {
                                stackAmmo(character, ammo, existingInventoryItem.get());
                            } else if (item instanceof KeyItem keyItem && keyItem.getKeyCategory() == KeyCategory.INK_RIBBON) {
                                stackInkRibbon(character, keyItem, existingInventoryItem.get());
                            } else if (!(item instanceof File)) {
                                addItemToInventory(character, item);
                            }
                        } else if (!(item instanceof File)) {
                            addItemToInventory(character, item);
                        }
                        if (item instanceof File file) {
                            readFile(character, file.getId());
                        }
                    } else {
                        //TODO: set string where "sout" was used ("you cannot carry any more items.")
                    }
                });
    }

    public void addItemToInventory(Character character, Item item) {
        Inventory inventoryItem = new Inventory();
        inventoryItem.setCharacter(character);
        inventoryItem.setItem(item);
        inventoryRepository.save(inventoryItem);
    }

    private void stackAmmo(Character character, Ammo ammo, Inventory existingInventoryItem) {
        Ammo existingAmmo = (Ammo) existingInventoryItem.getItem();
        int quantityToAdd = switch (ammo.getAmmoCategory()) {
            case HANDGUN_BULLETS -> 15;
            case SHOTGUN_SHELLS -> 7;
            case BOWGUN_BOLTS -> 18;
            case GRENADE_ROUNDS, ACID_ROUNDS, FLAME_ROUNDS -> 6;
            case MAGNUM_BULLETS -> 8;
            case MACHINEGUN_BULLETS -> 100;
        };
        existingAmmo.setAmmoQuantity(existingAmmo.getAmmoQuantity() + quantityToAdd);
        existingInventoryItem.setCharacter(character);
        existingInventoryItem.setItem(existingAmmo);
        inventoryRepository.save(existingInventoryItem);
    }

    private void stackInkRibbon(Character character, KeyItem keyItem, Inventory existingInventoryItem) {
        KeyItem existingKeyItem = (KeyItem) existingInventoryItem.getItem();
        int quantityToAdd = switch (keyItem.getName()) {
            case "Ink Ribbon" -> 3;
            case "Small Key" -> 1;
            default -> 0;
        };
        existingKeyItem.setKeyQuantity(existingKeyItem.getKeyQuantity() + quantityToAdd);
        existingInventoryItem.setCharacter(character);
        existingInventoryItem.setItem(existingKeyItem);
        inventoryRepository.save(existingInventoryItem);
    }

    public void itemBoxTransfer(Character character, Long itemId, boolean toItemBox) {
        inventoryRepository.findByCharacterAndItemId(character, itemId)
                .ifPresent(inventoryItem -> {
                    if (toItemBox) {
                        ItemBox itemBoxItem = new ItemBox();
                        itemBoxItem.setCharacters(character);
                        itemBoxItem.setItem(inventoryItem.getItem());
                        itemBoxService.addToItemBox(itemBoxItem);
                        inventoryRepository.delete(inventoryItem);
                    } else {
                        ItemBox itemBoxItem = itemBoxService.findByCharacterAndItemId(character, itemId)
                                .orElseThrow (() -> new RuntimeException("Item not found in item box."));
                        addItemToInventory(character, itemBoxItem.getItem());
                        itemBoxService.delete(itemBoxItem);
                    }
                });
    }

    public void useWeapon(Character character, Long weaponId) {
        inventoryRepository.findByCharacterAndItemId(character, weaponId)
                .ifPresent(inventoryWeapon -> {
                    if (inventoryWeapon.getItem() instanceof Weapon weapon) {
                        weaponService.weaponUseCount(weapon, 1);
                    }
                });
    }

    public String readFile(Character character, Long fileId) {
        return inventoryRepository.findByCharacterAndItemId(character, fileId)
                .map(inventoryFile -> inventoryFile.getItem().getDescription())
                .orElse(null);
    }

    public void darkRoom(Character character, Long filmId) {
        inventoryRepository.findByCharacterAndItemId(character, filmId)
                .ifPresent(inventoryFilm -> {
                    if (inventoryFilm.getItem() instanceof KeyItem film && film.getKeyCategory() == KeyCategory.FILM) {
                        File developedFile = keyService.developFilm(film);
                        if (developedFile != null) {
                            inventoryRepository.delete(inventoryFilm);
                            addItemToInventory(character, developedFile);
                            readFile(character, developedFile.getId());
                        }
                    }
                });
    }

    public void combineItems(Character character, Long itemAId, Long itemBId) {
        Item item1 = itemRepository.findById(itemAId)
                .orElse(null);
        Item item2 = itemRepository.findById(itemBId)
                .orElse(null);
        if (item1 != null && item2 != null) {
            if (item1 instanceof RecoveryItem herb1 && item2 instanceof RecoveryItem herb2) {
                combineHerbs(character, herb1, herb2);
            } else if ((item1 instanceof Weapon && item2 instanceof Ammo) || (item1 instanceof Ammo && item2 instanceof Weapon)) {
                combineWeaponWithAmmo(character, item1, item2);
            } else if ((item1 instanceof Weapon && item2 instanceof Parts) || (item1 instanceof Parts && item2 instanceof Weapon)) {
                combineWeaponWithParts(character, item1, item2);
            } else if (item1 instanceof KeyItem keyItem1 && item2 instanceof KeyItem keyItem2) {
                craftKeyItem(character, keyItem1, keyItem2);
            } else {
                //TODO: sout to string "you cannot combine these."
            }
        }
    }

    private void combineHerbs(Character character, RecoveryItem herb1, RecoveryItem herb2) {
        RecoveryItem combinedHerb = recoveryService.mixHerb(herb1, herb2);
        if (combinedHerb != null) {
            removeItemFromInventory(character, herb1.getId());
            removeItemFromInventory(character, herb2.getId());
            addItemToInventory(character, combinedHerb);
        }
    }

    private void combineWeaponWithAmmo(Character character, Item item1, Item item2) {
        Weapon weapon = null;
        Ammo ammo = null;

        if (item1 instanceof Weapon && item2 instanceof Ammo) {
            weapon = (Weapon) item1;
            ammo = (Ammo) item2;
        } else if (item1 instanceof Ammo && item2 instanceof Weapon) {
            ammo = (Ammo) item1;
            weapon = (Weapon) item2;
        }

        if (weapon != null) {
            ReloadRecord reloadResult = weaponService.reloadWeapon(weapon, ammo);
            Ammo returnedAmmo = reloadResult.returnedAmmo();
            if (returnedAmmo != null) {
                updateInventoryAmmo(character, returnedAmmo);
            }
            if (weapon.getWeaponCategory() != WeaponCategory.INFINITE_WEAPON && ammo.getAmmoQuantity() <= 0) {
                removeItemFromInventory(character, ammo.getId());
            }
        } else {
            //TODO: sout to string: "you cannot combine these."
        }
    }

    private void combineWeaponWithParts(Character character, Item item1, Item item2) {
        Weapon weapon = null;
        Parts parts = null;

        if (item1 instanceof Weapon && item2 instanceof Parts) {
            weapon = (Weapon) item1;
            parts = (Parts) item2;
        } else if (item1 instanceof Parts && item2 instanceof Weapon) {
            parts = (Parts) item1;
            weapon = (Weapon) item2;
        }

        if (weapon != null) {
            Weapon customWeapon = weaponService.upgradeWeapon(weapon, parts);
            if (customWeapon != null) {
                removeItemFromInventory(character, parts.getId());
                removeItemFromInventory(character, weapon.getId());
                addItemToInventory(character, customWeapon);
            }
        } else {
            //TODO: sout to string: "you cannot combine these."
        }
    }

    private void craftKeyItem(Character character, KeyItem item1, KeyItem item2) {
        KeyItem combinedKeyItem = keyService.combineKeyItems(item1, item2);
        if (combinedKeyItem != null) {
            removeItemFromInventory(character, item1.getId());
            removeItemFromInventory(character, item2.getId());
            addItemToInventory(character, combinedKeyItem);
        }
    }

    public void updateInventoryAmmo(Character character, Ammo returnedAmmo) {
        inventoryRepository.findByCharacterAndItemId(character, returnedAmmo.getId())
                .ifPresent(inventoryAmmo -> {
                    if (inventoryAmmo.getItem() instanceof Ammo ammo) {
                        ammo.setAmmoQuantity(ammo.getAmmoQuantity() + returnedAmmo.getAmmoQuantity());
                        inventoryRepository.save(inventoryAmmo);
                    }
                });
    }

    private void removeItemFromInventory(Character character, Long itemId) {
        inventoryRepository.findByCharacterAndItemId(character, itemId)
                .ifPresent(inventoryRepository::delete);
    }

}
