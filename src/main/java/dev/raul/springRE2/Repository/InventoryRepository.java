package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Inventories.Character;
import dev.raul.springRE2.Model.Inventories.Inventory;
import dev.raul.springRE2.Model.Items.Item;
import dev.raul.springRE2.Model.Items.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByCharacter(Character character);

}
