package dev.raul.springRE2.Model.Inventories;

import dev.raul.springRE2.Model.Item.Weaponry.Weapons.Weapon;

public class Inventory {

    private Characters characters;

    private Characters getCharacters() {
        return characters;
    }

    private void setCharacters(Characters characters) {
        this.characters = characters;
    }

    private Weapon equipWeapon(Weapon equippedWeapon) {
        return equippedWeapon;
    }

}
