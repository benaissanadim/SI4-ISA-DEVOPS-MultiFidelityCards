package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.NotEligibleClientException;
import fr.univcotedazur.multifidelitycards.exceptions.NotEnoughPointsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * OfferEligibility to test eligibility client for offers
 */
@Transactional
@Component
public class OfferEligibility {

    Logger log = LoggerFactory.getLogger(FidelityCardPayment.class);

    private final HistoryService clientHistory;

    @Autowired
    public OfferEligibility(HistoryService clientHistory) {
        this.clientHistory = clientHistory;
    }

    public void eligibleToOffer(FidelityCard card, Offer offer)
            throws NotEnoughPointsException, NotEligibleClientException {
        log.info("checking client {} eligibility to use parking ", card.getClient().getUsername());
        Client client = card.getClient();
        if(offer.getType().equals(OfferType.VFP_OFFER)
                && !client.getStatus().equals(ClientStatus.VFP)){
            log.warn("Client with fidelity card id {} is not eligible for this offer VFP"
                    , card.getId());
            throw new NotEligibleClientException("not eligible for VFP offer");
        }
        if(card.getPoints() < offer.getPoints()){
            log.warn("Client with fidelity card id {} has not enough points to use this offer"
                    , card.getId());
            throw new NotEnoughPointsException("not enough points");
        }
        this.clientHistory.addOfferUseToHistory(card, offer);
    }

     public void eligibleToGift(FidelityCard card, Gift gift)
             throws NotEligibleClientException, NotEnoughPointsException {
        Store store = gift.getStore();
        List<PurchaseAction> purchases = clientHistory.findPurchasesInStore(store, card);
         System.out.println(purchases);
        if (purchases.isEmpty() || purchases.size() < 2 ||
                !purchases.get(0).getCreatedAt().toLocalDate().equals(LocalDate.now())) {
            log.warn("You have to buy something in store {} to be eligible for gift", store.getName());
            throw new NotEligibleClientException("not eligible for gift");
        }
        eligibleToOffer(card, gift);
    }
}
