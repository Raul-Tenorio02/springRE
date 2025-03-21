package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Item.Item;
import dev.raul.springRE2.Model.Item.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemCategory(ItemCategory itemCategory);

}
