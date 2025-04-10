package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Items.Key.KeyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<KeyItem, Long> {
}
