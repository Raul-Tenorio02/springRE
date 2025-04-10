package dev.raul.springRE2.Service.ItemsServices.Key;

import dev.raul.springRE2.Model.Items.Files.File;
import dev.raul.springRE2.Model.Items.Key.KeyItem;

public interface KeyInterface {

    File developFilm(KeyItem keyItem);
    KeyItem combineKeyItems(KeyItem keyA, KeyItem keyB);

}
