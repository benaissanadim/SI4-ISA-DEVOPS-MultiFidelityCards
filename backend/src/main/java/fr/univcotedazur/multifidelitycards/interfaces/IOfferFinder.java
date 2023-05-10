package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Offer;
import fr.univcotedazur.multifidelitycards.exceptions.OfferNotFoundException;

/**
 * offer finder
 */
public interface IOfferFinder {
    Offer findById(Long offerId) throws OfferNotFoundException;
}
