package dev.raul.springRE2.Service.InventoryServices;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void setInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public Optional<Inventory> getInventoryById(Integer id){
        return inventoryRepository.findById(id);
    }

    public List<Inventory> getInventoryByCharacter(Character character) {
        return inventoryRepository.findByCharacter(character);
    }

    public List<Inventory> getInventory() {
        return inventoryRepository.findAll();
    }

    public void deleteFromInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }

}
