package dev.raul.springRE2.Model.Items.Weaponry.Weapons;

import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.AmmoCategory;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("WEAPON")
@NoArgsConstructor
public class Weapon extends Item {

    @Enumerated(EnumType.STRING)
    private WeaponCategory weaponCategory;

    private int magazine;
    private int maxCapacity;

    @Enumerated(EnumType.STRING)
    private FireType fireType;

    @Enumerated(EnumType.STRING)
    private AmmoCategory loadedAmmo;

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory weaponCategory) {
        super(id, name, description, itemCategory, iconPath);
        this.weaponCategory = weaponCategory;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory weaponCategory, int magazine, int maxCapacity, FireType fireType) {
        super(id, name, description, itemCategory, iconPath);
        this.weaponCategory = weaponCategory;
        this.magazine = magazine;
        this.maxCapacity = maxCapacity;
        this.fireType = fireType;
    }

    public Weapon(Long id, String name, String description, ItemCategory itemCategory, String iconPath, WeaponCategory weaponCategory, int magazine, int maxCapacity, FireType fireType, AmmoCategory loadedAmmo) {
        super(id, name, description, itemCategory, iconPath);
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

    public int getFireRate() {
        return (this.fireType == FireType.BURST) ? 3 : 1;
    }

}
