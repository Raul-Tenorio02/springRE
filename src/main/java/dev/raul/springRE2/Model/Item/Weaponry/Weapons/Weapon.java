package dev.raul.springRE2.Model.Item.Weaponry.Weapons;

import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.AmmoCategory;
import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weapon_tb")
@NoArgsConstructor
public class Weapon extends Item {

    private int magazine;
    private int maxCapacity;

    @Enumerated(EnumType.STRING)
    private WeaponCategory weaponCategory;

    @Enumerated(EnumType.STRING)
    private AmmoCategory loadedAmmo;

    @Enumerated(EnumType.STRING)
    private FireType fireType;

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory typeWeapon) {
        super(id, name, description, itemCategory, iconPath);
        this.weaponCategory = typeWeapon;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory typeWeapon, int magazine, int maxCapacity, FireType fireType) {
        super(id, name, description, itemCategory, iconPath);
        this.weaponCategory = typeWeapon;
        this.magazine = magazine;
        this.maxCapacity = maxCapacity;
        this.fireType = fireType;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory typeWeapon, int magazine, int maxCapacity, FireType fireType, AmmoCategory loadedAmmo) {
        super(id, name, description, itemCategory, iconPath);
        this.weaponCategory = typeWeapon;
        this.magazine = magazine;
        this.maxCapacity = maxCapacity;
        this.fireType = fireType;
        this.loadedAmmo = loadedAmmo;
    }

    public int getMagazine() {
        return magazine;
    }

    public void setMagazine(int magazine) {
        this.magazine = magazine;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public WeaponCategory getWeaponCategory() {
        return weaponCategory;
    }

    public void setWeaponCategory(WeaponCategory weaponCategory) {
        this.weaponCategory = weaponCategory;
    }

    public AmmoCategory getLoadedAmmo() {
        return loadedAmmo;
    }

    public void setLoadedAmmo(AmmoCategory loadedAmmo) {
        this.loadedAmmo = loadedAmmo;
    }

    public FireType getFireType() {
        return fireType;
    }

    public void setFireType(FireType fireType) {
        this.fireType = fireType;
    }
}
