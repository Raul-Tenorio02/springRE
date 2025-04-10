package dev.raul.springRE2.Service.ItemsServices.Key;

import dev.raul.springRE2.Model.Items.Files.File;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Model.Items.Key.KeyCategory;
import dev.raul.springRE2.Model.Items.Key.KeyItem;
import dev.raul.springRE2.Repository.KeyRepository;
import dev.raul.springRE2.Service.ItemsServices.ItemService;

import java.util.Map;
import java.util.Set;

public class KeyService implements KeyInterface {

    private final ItemService itemService;
    private final KeyRepository keyRepository;

    public KeyService(ItemService itemService, KeyRepository keyRepository) {
        this.itemService = itemService;
        this.keyRepository = keyRepository;
    }

    @Override
    public File developFilm(KeyItem film) {
        if (film.getKeyCategory() == KeyCategory.FILM) {
            if (film.getId() == 38L) return (File) itemService.findById(102L).orElse(null);
            else if (film.getId() == 39L) return (File) itemService.findById(103L).orElse(null);
            else if (film.getId() == 40L) return (File) itemService.findById(104L).orElse(null);
            else if (film.getId() == 41L) return (File) itemService.findById(105L).orElse(null);
            else return null;
        }
        return null;
    }

    @Override
    public KeyItem combineKeyItems(KeyItem keyA, KeyItem keyB){
        if (keyA.getItemCategory() != ItemCategory.KEY && keyB.getItemCategory() != ItemCategory.KEY) {
            return null;
        }
        Item plasticBomb = itemService.findById(47L).orElse(null);
        Item detonator = itemService.findById(48L).orElse(null);
        Item bombDet = itemService.findById(51L).orElse(null);
        Item blueStoneL = itemService.findById(61L).orElse(null);
        Item blueStoneR = itemService.findById(62L).orElse(null);
        Item jaguarStone = itemService.findById(63L).orElse(null);
        if (plasticBomb == null || detonator == null || bombDet == null || blueStoneL == null || blueStoneR == null || jaguarStone == null){
            return null;
        }

        Set<Item> blueStoneSet = Set.of(plasticBomb, detonator);
        Set<Item> bombDetonatorSet = Set.of(blueStoneL, blueStoneR);

        Map<Set<Item>, KeyItem> keyCrafts = Map.of(
                blueStoneSet, (KeyItem) jaguarStone,
                bombDetonatorSet, (KeyItem) bombDet
        );

        Set<Item> keySet = Set.of(keyA, keyB);

        for (Set<Item> combinationSet : keyCrafts.keySet()) {
            if (keySet.containsAll(combinationSet) && combinationSet.containsAll(keySet)) {
                return keyCrafts.get(combinationSet);
            }
        }
        return null;
    }

}
