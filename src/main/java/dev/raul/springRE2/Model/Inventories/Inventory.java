package dev.raul.springRE2.Model.Inventories;

import dev.raul.springRE2.Model.Items.Item;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory_tb")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
