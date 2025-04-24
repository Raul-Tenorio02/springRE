package dev.raul.springRE2.Service.ItemsServices.Weaponry;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.AmmoCategory;
import dev.raul.springRE2.Model.Items.Weaponry.WeaponParts.Parts;
import dev.raul.springRE2.Model.Items.Weaponry.WeaponParts.PartsCategory;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.ReloadRecord;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.Weapon;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.WeaponCategory;
import dev.raul.springRE2.Repository.WeaponRepository;
import dev.raul.springRE2.Service.ItemsServices.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@Transactional
public class WeaponService implements WeaponInterface {

    private final ItemService itemService;
    private final WeaponRepository weaponRepository;

    public WeaponService(ItemService itemService, WeaponRepository weaponRepository) {
        this.itemService = itemService;
        this.weaponRepository = weaponRepository;
    }

    @Override
    public ReloadRecord reloadWeapon(Weapon weapon, Ammo ammo) {
        if (!isWeapon(weapon)) {
            return new ReloadRecord(weapon, null);
        }

        if (!isCompatibleAmmo(weapon, ammo)) {
            System.out.println("\nThere is no need to combine these.");
            return new ReloadRecord(weapon, null);
        }

        Ammo returnedAmmo = Optional.ofNullable(weapon.getLoadedAmmo())
                .filter(loaded -> weapon.getWeaponCategory() == WeaponCategory.GRENADE_LAUNCHER && loaded != ammo.getAmmoCategory())
                .map(loaded -> unloadAmmo(weapon))
                .orElse(null);
        weapon.setLoadedAmmo(ammo.getAmmoCategory());

        Integer currentMagazine = weapon.getMagazine();
        int ammoNeeded = weapon.getMaxCapacity() - (currentMagazine != null ? currentMagazine : 0);
        weapon.setMagazine((currentMagazine != null ? currentMagazine : 0) + Math.min(ammo.getAmmoQuantity(), ammoNeeded));
        ammo.setAmmoQuantity(ammo.getAmmoQuantity() - ammoNeeded);

        weaponRepository.save(weapon);

        return new ReloadRecord(weapon, returnedAmmo);
    }

    @Override
    public Weapon upgradeWeapon(Weapon weapon, Parts part) {
        if (weapon.getItemCategory() != ItemCategory.WEAPON || part.getItemCategory() != ItemCategory.PART) {
            System.out.println("These items cannot be combined");
            return null;
        }

        Map<Long, Map<PartsCategory, Supplier<Weapon>>> upgradeMap = Map.of(
                2L, Map.of(PartsCategory.HANDGUN_PARTS, () -> (Weapon) itemService.findById(3L).orElse(null)),
                5L, Map.of(PartsCategory.SHOTGUN_PARTS, () -> (Weapon) itemService.findById(6L).orElse(null)),
                11L, Map.of(PartsCategory.MAGNUM_PARTS, () -> (Weapon) itemService.findById(12L).orElse(null))
        );

        Weapon upgradedWeapon = Optional.ofNullable(upgradeMap.get(weapon.getId()))
                .map(parts -> parts.get(part.getPartsCategory()))
                .map(Supplier::get)
                .orElse(null);

        if (upgradedWeapon != null) {
            weaponRepository.save(upgradedWeapon);
        }

        return upgradedWeapon;
    }

    @Override
    public Ammo unloadAmmo(Weapon weapon) {
        if (weapon.getLoadedAmmo() == null) return null;
        Map<AmmoCategory, Supplier<Ammo>> ammoSupplier = Map.of(
                AmmoCategory.GRENADE_ROUNDS, () -> (Ammo) itemService.findById(25L).orElse(null),
                AmmoCategory.ACID_ROUNDS, () -> (Ammo) itemService.findById(26L).orElse(null),
                AmmoCategory.FLAME_ROUNDS, () -> (Ammo) itemService.findById(27L).orElse(null)
        );
        return Optional.ofNullable(ammoSupplier.get(weapon.getLoadedAmmo()))
                .map(Supplier::get)
                .map(ammo -> {
                    ammo.setAmmoCategory(weapon.getLoadedAmmo());
                    ammo.setAmmoQuantity(Optional.ofNullable(weapon.getMagazine()).orElse(0));
                    weapon.setMagazine(0);
                    weaponRepository.save(weapon);
                    return ammo;
                })
                .orElse(null);
    }

    // extension of useWeapon() method from Inventory
    //TODO: create section to handle specific grenade rounds behavior
    @Override
    public void weaponUseCount(Weapon weapon, int count) {
        if (weapon.getWeaponCategory() == WeaponCategory.KNIFE) {
            System.out.println("\nYou've stabbed an enemy!");
        } else {
            int bulletsUsed = count * weapon.getFireRate();
            if (isInfiniteWeapon(weapon)) {
                System.out.println("\nYou've shot an enemy with your \"" + weapon.getName() + "\"!");
                return;
            }
            Integer currentMagazine = weapon.getMagazine();
            if (currentMagazine == null || currentMagazine == 0) {
                System.out.println("\nYour weapon is empty!");
            } else {
                if (bulletsUsed >= currentMagazine) {
                    weapon.setMagazine(0);
                    System.out.println("\nYou've shot an enemy with your \"" + weapon.getName() + "\"! Now your weapon is empty");
                } else {
                    weapon.setMagazine(currentMagazine - bulletsUsed);
                    System.out.println("\nYou've shot an enemy with your \"" + weapon.getName() + "\"!");
                }
            }
            weaponRepository.save(weapon);
        }
    }

    private boolean isWeapon(Item item) {
        return EnumSet.of(ItemCategory.WEAPON, ItemCategory.SPECIAL).contains(item.getItemCategory());
    }

    private boolean isInfiniteWeapon(Weapon weapon) {
        return Optional.ofNullable(weapon.getWeaponCategory())
                .map(type -> type == WeaponCategory.INFINITE_WEAPON)
                .orElse(false);
    }

    private boolean isCompatibleAmmo(Weapon weapon, Ammo ammo) {
        Map<WeaponCategory, Set<AmmoCategory>> ammoMap = Map.of(
                WeaponCategory.HANDGUN, Set.of(AmmoCategory.HANDGUN_BULLETS),
                WeaponCategory.SHOTGUN, Set.of(AmmoCategory.SHOTGUN_SHELLS),
                WeaponCategory.BOWGUN, Set.of(AmmoCategory.BOWGUN_BOLTS),
                WeaponCategory.GRENADE_LAUNCHER, Set.of(AmmoCategory.GRENADE_ROUNDS, AmmoCategory.ACID_ROUNDS, AmmoCategory.FLAME_ROUNDS),
                WeaponCategory.MAGNUM, Set.of(AmmoCategory.MAGNUM_BULLETS),
                WeaponCategory.MACHINE_GUN, Set.of(AmmoCategory.MACHINEGUN_BULLETS)
        );
        return ammoMap.getOrDefault(weapon.getWeaponCategory(), Set.of()).contains(ammo.getAmmoCategory());
    }

}
