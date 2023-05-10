package fr.univcotedazur.multifidelitycards.interfaces;

import java.time.YearMonth;
import java.util.Map;

/**
 * store history interface
 */
public interface IStoreHistory {
    Map<YearMonth, Double> getPurchaseHistoryPerMonth(Long storeId);
    Map<YearMonth, Integer> getGiftsHistoryPerMonth(Long storeId);
}
