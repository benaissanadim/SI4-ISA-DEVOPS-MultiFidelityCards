package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.*;

/**
 * offer manager interface
 */
public interface IOfferManager {
    void deleteOffer(Long offerId) throws OfferNotFoundException;
    Gift createGift(String name,String description, int points, Long store, OfferType type) throws StoreNotFoundException;
    ParkingOffer createParkingOffer(String name,String description, int points, int minutes,
            Long geographicZoneId, OfferType type) throws GeographicZoneNotFoundException;

}
