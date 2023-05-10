package fr.univcotedazur.multifidelitycards.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Gift class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Gift extends Offer{

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Gift(String name,String description, int points, Store store, OfferType type){
        super(name,description, points, type);
        this.store = store;

    }

}
