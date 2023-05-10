package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.*;


/**
 * collective offer use
 */
public interface ICollectiveOfferUse {

    /**
     * create collective offer
     * @param description description
     * @param points points
     * @param store store
     * @param type type
     * @return collective offer
     */
    CollectiveOffer createCollectiveOffer(String description, int points, GeographicZone store, OfferType type) ;

}
