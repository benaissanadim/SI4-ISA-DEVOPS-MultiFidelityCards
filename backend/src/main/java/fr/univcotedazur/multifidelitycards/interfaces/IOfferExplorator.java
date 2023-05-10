package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.*;

import java.util.List;

/**
 * offer explorator
 */
public interface IOfferExplorator {

    List<Offer> exploreOffersByGeographicZone(Long geographicZoneId);
    List<Gift> exploreAllGifts();
    List<ParkingOffer> exploreAllCollectiveOffers();

}
