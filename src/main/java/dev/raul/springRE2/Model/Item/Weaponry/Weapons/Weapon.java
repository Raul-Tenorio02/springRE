package dev.raul.springRE2.Model.Item.Weaponry.Weapons;

import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.AmmoCategory;
import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import dev.raul.springRE2.Model.Item.Weaponry.WeaponParts.Parts;
import dev.raul.springRE2.Model.Item.Weaponry.WeaponParts.PartsCategory;
import dev.raul.springRE2.Service.ItemService;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Entity
@DiscriminatorValue("WEAPON")
@NoArgsConstructor
@Component
public class Weapon extends Item implements WeaponInterface {

    private final ItemService itemService;

    @Enumerated(EnumType.STRING)
    private WeaponCategory weaponCategory;

    private int magazine;
    private int maxCapacity;

    @Enumerated(EnumType.STRING)
    private FireType fireType;

    @Enumerated(EnumType.STRING)
    private AmmoCategory loadedAmmo;

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, WeaponCategory weaponCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.weaponCategory = weaponCategory;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, WeaponCategory weaponCategory, int magazine, int maxCapacity, FireType fireType) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.weaponCategory = weaponCategory;
        this.magazine = magazine;
        this.maxCapacity = maxCapacity;
        this.fireType = fireType;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, ItemService itemService, WeaponCategory weaponCategory, int magazine, int maxCapacity, FireType fireType, AmmoCategory loadedAmmo) {
        super(id, name, description, itemCategory, iconPath);
        this.itemService = itemService;
        this.weaponCategory = weaponCategory;
        this.magazine = magazine;
        this.maxCapacity = maxCapacity;
        this.fireType = fireType;
        this.loadedAmmo = loadedAmmo;
    }

    public int getMagazine() { return magazine; }

    public void setMagazine(int magazine) { this.magazine = magazine; }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public WeaponCategory getWeaponCategory() { return weaponCategory; }

    public void setWeaponCategory(WeaponCategory weaponCategory) { this.weaponCategory = weaponCategory; }

    public AmmoCategory getLoadedAmmo() { return loadedAmmo; }

    public void setLoadedAmmo(AmmoCategory loadedAmmo) { this.loadedAmmo = loadedAmmo; }

    public FireType getFireType() { return fireType; }

    public void setFireType(FireType fireType) { this.fireType = fireType; }

    private int getFireRate() {
        return (this.fireType == FireType.BURST) ? 3 : 1;
    }

    private boolean isWeapon() {
        return EnumSet.of(ItemCategory.WEAPON, ItemCategory.SPECIAL).contains(this.getItemCategory());
    }

    private boolean isInfiniteWeapon() {
        return Optional.ofNullable(this.getWeaponCategory())
                .map(type -> type == WeaponCategory.INFINITE_WEAPON)
                .orElse(false);
    }

    @Override
    public ReloadRecord reloadWeapon(Ammo ammo) {
        if (!isWeapon()) {
            return new ReloadRecord(this, null);
        }

        if (!isCompatibleAmmo(ammo)) {
            System.out.println("\nThere is no need to combine these.");
            return new ReloadRecord(this, null);
        }

        Ammo returnedAmmo = Optional.ofNullable(loadedAmmo)
                .filter(loaded -> weaponCategory == WeaponCategory.GRENADE_LAUNCHER && loaded != ammo.getAmmoCategory())
                .map(loaded -> unloadAmmo())
                .orElse(null);

        loadedAmmo = ammo.getAmmoCategory();

        int ammoNeeded = getMaxCapacity() - getMagazine();
        this.magazine += Math.min(ammo.getAmmoQuantity(), ammoNeeded);
        ammo.setAmmoQuantity(ammo.getAmmoQuantity() - ammoNeeded);

        return new ReloadRecord(this, returnedAmmo);

    }

    @Override
    public Weapon upgradeWeapon(Parts part) {
        if (this.getItemCategory() != ItemCategory.WEAPON || part.getItemCategory() != ItemCategory.PART) {
            System.out.println("These items cannot be combined");
            return null;
        }

        Map<Long, Map<PartsCategory, Supplier<Weapon>>> upgradeMap = Map.of(
                2, Map.of(PartsCategory.HANDGUN_PARTS, itemService.findById().orElse(null)),
                5, Map.of(PartsCategory.SHOTGUN_PARTS, itemService.findById().orElse(null)),
                11, Map.of(PartsCategory.MAGNUM_PARTS, itemService.findById().orElse(null))
        );

        return Optional.ofNullable(upgradeMap.get(this.getId()))
                .map(parts -> parts.get(part.getPartsCategory()))
                .map(Supplier::get)
                .orElse(null);
    }

    private boolean isCompatibleAmmo(Ammo ammo) {
        Map<WeaponCategory, Set<AmmoCategory>> ammoMap = Map.of(
                WeaponCategory.HANDGUN, Set.of(AmmoCategory.HANDGUN_BULLETS),
                WeaponCategory.SHOTGUN, Set.of(AmmoCategory.SHOTGUN_SHELLS),
                WeaponCategory.BOWGUN, Set.of(AmmoCategory.BOWGUN_BOLTS),
                WeaponCategory.GRENADE_LAUNCHER, Set.of(AmmoCategory.GRENADE_ROUNDS, AmmoCategory.ACID_ROUNDS, AmmoCategory.FLAME_ROUNDS),
                WeaponCategory.MAGNUM, Set.of(AmmoCategory.MAGNUM_BULLETS),
                WeaponCategory.MACHINE_GUN, Set.of(AmmoCategory.MACHINEGUN_BULLETS)
        );
        return ammoMap.getOrDefault(this.weaponCategory, Set.of()).contains(ammo.getAmmoCategory());
    }

    @Override
    public Ammo unloadAmmo() {
        if (loadedAmmo == null) return null;

        Map<AmmoCategory, Supplier<Ammo>> ammoSupplier = Map.of(
                AmmoCategory.GRENADE_ROUNDS, itemService.findById().orElse(null),
                AmmoCategory.ACID_ROUNDS, itemService.findById().orElse(null),
                AmmoCategory.FLAME_ROUNDS, itemService.findById().orElse(null)
        );

        return Optional.ofNullable(ammoSupplier.get(loadedAmmo))
                .map(Supplier::get)
                .map(ammo -> {
                    ammo.setAmmoCategory(loadedAmmo);
                    ammo.setAmmoQuantity(this.magazine);
                    this.magazine = 0;
                    return ammo;
                })
                .orElse(null);
    }

    // extension of useWeapon() method from Inventory
    //TODO: create section to handle specific grenade rounds behavior
    @Override
    public void weaponUseCount(int count) {
        if (this.getWeaponCategory() == WeaponCategory.KNIFE) {
            System.out.println("\nYou've stabbed an enemy!");
        } else {
            int bulletsUsed = count * getFireRate();
            if (isInfiniteWeapon()) {
                System.out.println("\nYou've shot an enemy with your \"" + getName() + "\"!");
                return;
            }
            if (this.getMagazine() == 0) {
                System.out.println("\nYour weapon is empty!");
            } else {
                if (bulletsUsed >= this.getMagazine()) {
                    setMagazine(0);
                    System.out.println("\nYou've shot an enemy with your \"" + getName() + "\"! Now your weapon is empty");
                } else {
                    setMagazine(getMagazine() - bulletsUsed);
                    System.out.println("\nYou've shot an enemy with your \"" + getName() + "\"!");
                }
            }
        }
    }

}
