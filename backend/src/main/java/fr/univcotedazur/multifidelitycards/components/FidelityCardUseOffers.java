package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.ParkingProxy;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.*;
import fr.univcotedazur.multifidelitycards.interfaces.IFidelityCardOfferUse;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FidelityCardUseOffer to benifit from all types of offers
 */
@Transactional
@Component
public class FidelityCardUseOffers implements IFidelityCardOfferUse {

    Logger log = LoggerFactory.getLogger(FidelityCardUseOffers.class);

    private final FidelityCardHandlerService fidelityCardHandler;
    private final OfferHandlerService offerFinder;
    private final OfferEligibility offerEligibility;
    private final ParkingProxy parkingProxy;

    @Autowired
    public FidelityCardUseOffers(FidelityCardHandlerService fidelityCardHandler,
            FidelityCardRepository fidelityCardRepository, OfferHandlerService offerFinder,
            OfferEligibility offerEligibility, ParkingProxy parkingProxy) {
        this.fidelityCardHandler = fidelityCardHandler;
        this.offerFinder = offerFinder;
        this.offerEligibility = offerEligibility;
        this.parkingProxy = parkingProxy;
    }

    public FidelityCard useParking(Long parkingId, Long cardId)
            throws NotEnoughPointsException, NotEligibleClientException, OfferNotFoundException,
            FidelityCardNotFoundException, ParkingProxyException {

        ParkingOffer parkingOffer = (ParkingOffer) offerFinder.findById(parkingId);
        FidelityCard card = fidelityCardHandler.findById(cardId);
        offerEligibility.eligibleToOffer(card, parkingOffer);
        if(!parkingProxy.callParking(parkingOffer, card.getClient())){
            throw new ParkingProxyException("cannot call parking server to use parking");
        }
        card = fidelityCardHandler.retrievePoints(card, parkingOffer.getPoints());
        log.info("Client with fidelity card {} used a parking with SUCCESS", card.getNumber());
        return card;
    }


    public FidelityCard receiveGift(Long giftId, Long cardId)
            throws NotEnoughPointsException, NotEligibleClientException, OfferNotFoundException,
            FidelityCardNotFoundException {
        Gift gift = (Gift) offerFinder.findById(giftId);
        FidelityCard card = fidelityCardHandler.findById(cardId);
        Store store = gift.getStore();
        log.info("checking client {} eligibility to receive gift from store {}", card.getClient().getUsername(),
                store.getName());
        offerEligibility.eligibleToGift(card, gift);
        fidelityCardHandler.retrievePoints(card, gift.getPoints());
        log.info("Client with fidelity card id {} received a gift from store with SUCCESS {}", card.getId(),
                store.getName());
        return card;
    }

}