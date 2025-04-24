package dev.raul.springRE2.Model.Inventories;

import dev.raul.springRE2.Model.Items.Item;
import jakarta.persistence.*;

@Entity
@Table(name = "item_box_tb")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getCharacters() {
        return characters;
    }

    public void setCharacters(Character characters) {
        this.characters = characters;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
