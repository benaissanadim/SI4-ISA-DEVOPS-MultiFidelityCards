package fr.univcotedazur.multifidelitycards.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * CollectiveOffer class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class CollectiveOffer extends Offer{

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private GeographicZone geographicZone;

    public CollectiveOffer(String name,String description, int points, GeographicZone geographicZone, OfferType type) {
        super(name,description, points, type);
        this.geographicZone = geographicZone;
    }

}
