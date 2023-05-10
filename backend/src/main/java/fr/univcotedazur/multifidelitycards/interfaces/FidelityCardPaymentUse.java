package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.exceptions.*;

/**
 * Pay with fidelity card
 */
public interface FidelityCardPaymentUse {

    FidelityCard pay(Long cardId, double amount, String store)
            throws FidelityCardNotFoundException, InvalidPaymentException, StoreNotFoundException;

    FidelityCard recharge(Long cardId, double amount, String creditCard) throws InvalidPaymentException,
            FidelityCardNotFoundException;
}

