package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory addToInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> getInventoryById(Integer id){
        return inventoryRepository.findById(id);
    }

    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    public void deleteInventoryItem(Integer id) {
        inventoryRepository.deleteById(id);
    }

}
