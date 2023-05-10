package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.exceptions.*;

/**
 * fidelity card offer use
 */
public interface IFidelityCardOfferUse {

    /**
     * use parking
     * @param parkingId id of parking
     * @param cardId id of card
     * @return fidelity card
     * @throws NotEnoughPointsException exception not enough points
     * @throws NotEligibleClientException exception not eligible
     * @throws OfferNotFoundException exception offer not found
     * @throws FidelityCardNotFoundException exception card not found
     * @throws ParkingProxyException exception parking
     */
    FidelityCard useParking(Long parkingId, Long cardId)
            throws NotEnoughPointsException, NotEligibleClientException, OfferNotFoundException,
            FidelityCardNotFoundException, ParkingProxyException;

    /**
     * receive parking
     * @param giftId id of gift
     * @param cardId id of card
     * @return card
     * @throws NotEnoughPointsException exception not enough points
     * @throws NotEligibleClientException exception not eligible
     * @throws OfferNotFoundException exception offer not found
     * @throws FidelityCardNotFoundException exception card not found
     */
    FidelityCard receiveGift(Long giftId, Long cardId)
            throws NotEnoughPointsException, NotEligibleClientException, OfferNotFoundException,
            FidelityCardNotFoundException ;
}
