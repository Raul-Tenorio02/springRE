package dev.raul.springRE2.Model.Item.Weaponry.Weapons;

import dev.raul.springRE2.Model.Item.Weaponry.Ammunition.Ammo;

public record ReloadRecord(Weapon weapon, Ammo returnedAmmo) {

}
