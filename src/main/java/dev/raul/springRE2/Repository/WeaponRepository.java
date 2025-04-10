package dev.raul.springRE2.Repository;

import dev.raul.springRE2.Model.Items.Weaponry.Weapons.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {
}
