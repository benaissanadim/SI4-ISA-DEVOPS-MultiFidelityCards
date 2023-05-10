package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.PurchaseAction;
import fr.univcotedazur.multifidelitycards.entities.Store;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * history added interface
 */
public interface IHistoryAdder {

    Map<LocalDate, Long> findNumberPurchasesLastWeekPerDay(FidelityCard card, LocalDateTime date);
    List<PurchaseAction> findPurchasesInStore(Store store, FidelityCard card);
}
