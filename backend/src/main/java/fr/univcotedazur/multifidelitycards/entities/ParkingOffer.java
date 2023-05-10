package fr.univcotedazur.multifidelitycards.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * ParkingOffer class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ParkingOffer extends CollectiveOffer {

    private int minutes;

    public ParkingOffer(String name, String description, int points, GeographicZone geographicZone, OfferType type, int minutes) {
        super(name, description, points, geographicZone, type);
        this.minutes = minutes;
    }
}
