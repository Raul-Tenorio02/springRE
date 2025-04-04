package dev.raul.springRE2.Service;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
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
    public List<Item> getAll() {return itemRepository.findAll();}

    //FIND BY ID
    public Optional<Item> findById(Long id) {return itemRepository.findById(id);}

    // FIND BY CATEGORY
    public List<Item> findByItemCategory(ItemCategory category) {
        return itemRepository.findByItemCategory(category);
    }

}