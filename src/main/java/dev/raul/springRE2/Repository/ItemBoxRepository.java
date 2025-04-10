package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Inventories.ItemBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemBoxRepository extends JpaRepository<ItemBox, Integer> {
}
