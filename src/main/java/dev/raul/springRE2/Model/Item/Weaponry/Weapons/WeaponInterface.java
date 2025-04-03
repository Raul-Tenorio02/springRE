package dev.raul.springRE2.Model.Item.Weaponry.Weapons;

import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Item.Weaponry.WeaponParts.Parts;

public interface WeaponInterface {

    ReloadRecord reloadWeapon(Ammo ammo);
    Ammo unloadAmmo();
    void weaponUseCount(int count);
    Weapon upgradeWeapon(Parts part);

}
