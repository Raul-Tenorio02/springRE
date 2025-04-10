package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Items.Recovery.RecoveryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryRepository extends JpaRepository<RecoveryItem, Long> {
}
