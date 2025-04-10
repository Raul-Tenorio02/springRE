package dev.raul.springRE2.Model.Inventories;

import dev.raul.springRE2.Model.Items.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_box_tb")
@Getter
@Setter
public class ItemBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character characters;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

}
