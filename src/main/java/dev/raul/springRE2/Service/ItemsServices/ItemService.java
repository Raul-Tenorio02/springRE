package dev.raul.springRE2.Service.ItemsServices;

import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import dev.raul.springRE2.Repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    //READ
    public List<Item> findAllItems() {return itemRepository.findAll();}

    //FIND BY ID
    public Optional<Item> findById(Long id) {return itemRepository.findById(id);}

    // FIND BY CATEGORY
    public List<Item> findByItemCategory(ItemCategory category) {
        return itemRepository.findItemByItemCategory(category);
    }

}