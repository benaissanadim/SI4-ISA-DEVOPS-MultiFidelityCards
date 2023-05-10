package fr.univcotedazur.multifidelitycard.cli.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class CliGift extends CliOffer{
    private String store;

    public CliGift(String name,String description, int points,String store, String type) {
        super(name, description, points, type);
        this.store = store;
    }

    public String toString(){
        return "Gift : {name=" + name + ", description=" + description + ", points=" + points + ", store=" + store + "}";
    }

}
