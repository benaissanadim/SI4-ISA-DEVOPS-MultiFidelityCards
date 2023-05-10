package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * history finder
 */
public interface IHistoryFinder {

    void addPurchaseToHistory(FidelityCard fidelityCard, double amount, Store store);
    void addOfferUseToHistory(FidelityCard fidelityCard, Offer offer);
}
