package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Inventories.ItemBox;
import dev.raul.springRE2.Repository.ItemBoxRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemBoxService {

    private final ItemBoxRepository itemBoxRepository;

    public ItemBoxService(ItemBoxRepository itemBoxRepository) {
        this.itemBoxRepository = itemBoxRepository;
    }

    public ItemBox addToItemBox(ItemBox itemBox) {
        return itemBoxRepository.save(itemBox);
    }

    public Optional<ItemBox> getInItemBoxById(Integer id) {
        return itemBoxRepository.findById(id);
    }

    public List<ItemBox> getAllItemBoxItems() {
        return itemBoxRepository.findAll();
    }

    public void deleteItemBoxItem(Integer id) {
        itemBoxRepository.deleteById(id);
    }

}
