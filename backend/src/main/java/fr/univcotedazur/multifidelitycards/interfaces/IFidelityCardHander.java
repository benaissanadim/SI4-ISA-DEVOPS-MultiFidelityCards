package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.exceptions.InvalidPaymentException;
import fr.univcotedazur.multifidelitycards.exceptions.NotEnoughPointsException;

public interface IFidelityCardHander {
    FidelityCard retrievePoints(FidelityCard fidelityCard, int nbPoints) throws NotEnoughPointsException;
    FidelityCard addPointsFromAmount(FidelityCard fidelityCard, double amount);
    FidelityCard addAmount(FidelityCard fidelityCard, double amount);
    FidelityCard retreiveAmount(FidelityCard fidelityCard, double amount) throws InvalidPaymentException;
}
