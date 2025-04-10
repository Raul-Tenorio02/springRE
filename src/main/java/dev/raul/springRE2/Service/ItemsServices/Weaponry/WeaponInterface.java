package dev.raul.springRE2.Service.ItemsServices.Weaponry;

import dev.raul.springRE2.Model.Items.Weaponry.Ammunition.Ammo;
import dev.raul.springRE2.Model.Items.Weaponry.WeaponParts.Parts;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.ReloadRecord;
import dev.raul.springRE2.Model.Items.Weaponry.Weapons.Weapon;

public interface WeaponInterface {

    ReloadRecord reloadWeapon(Weapon weapon, Ammo ammo);
    Ammo unloadAmmo(Weapon weapon);
    void weaponUseCount(Weapon weapon, int count);
    Weapon upgradeWeapon(Weapon weapon, Parts part);

}
