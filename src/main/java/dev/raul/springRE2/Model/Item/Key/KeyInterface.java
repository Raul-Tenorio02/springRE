package dev.raul.springRE2.Model.Item.Key;

import dev.raul.springRE2.Model.Item.Files.File;

public interface KeyInterface {

    File developFilm(KeyItem keyItem);
    KeyItem combineKeyItems(KeyItem otherKeyItem);

}
