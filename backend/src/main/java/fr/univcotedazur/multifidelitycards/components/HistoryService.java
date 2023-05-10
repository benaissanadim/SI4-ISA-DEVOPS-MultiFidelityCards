package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.interfaces.IHistoryAdder;
import fr.univcotedazur.multifidelitycards.interfaces.IHistoryFinder;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreHistory;
import fr.univcotedazur.multifidelitycards.repositories.OfferUseRepository;
import fr.univcotedazur.multifidelitycards.repositories.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HistoryService for adding to history and searching client and store history
 */
@Transactional
@Component
public class HistoryService implements IStoreHistory,  IHistoryAdder, IHistoryFinder {

    private final PurchaseRepository purchaseRepository;
    private final OfferUseRepository offerUseRepository;

    Logger log = LoggerFactory.getLogger(HistoryService.class);

    @Autowired
    public HistoryService(PurchaseRepository purchaseRepository, OfferUseRepository offerUseRepository) {
        this.purchaseRepository = purchaseRepository;
        this.offerUseRepository = offerUseRepository;
    }

    @Override
    public Map<LocalDate, Long> findNumberPurchasesLastWeekPerDay(FidelityCard card, LocalDateTime date) {

        List<PurchaseAction> purchases = purchaseRepository.findPurchasesByFidelityCardAfter(card,
                date.minusWeeks(1));
        Map<LocalDate, Long> map = purchases.stream()
                .collect(Collectors.groupingBy(p ->p.getCreatedAt().toLocalDate(),
                        Collectors.counting()));
        log.info("finding nb purchases per day last week {} for client {}", map, card.getClient().getUsername());
        return map;
    }

    @Override
    public List<PurchaseAction> findPurchasesInStore(Store store, FidelityCard card) {
        //
        List<PurchaseAction> p = purchaseRepository.findCardPurchasesInStore(card, store);
        log.info("finding purchases in store {} for fidelity card {} : {}",
                store.getName(), card.getNumber(), p);
        return p;
    }

    @Override
    public void addPurchaseToHistory(FidelityCard fidelityCard, double amount, Store store){
        log.info("adding purchase in store {} in client history", store.getName());
        PurchaseAction t = new PurchaseAction(fidelityCard, amount, store);
        fidelityCard.addTransaction(t);
    }

    @Override
    public void addOfferUseToHistory(FidelityCard fidelityCard, Offer offer){
        log.info("adding offer used in client history");
        OfferUseAction t = new OfferUseAction(fidelityCard, offer);
        fidelityCard.addTransaction(t);
    }


    public Map<YearMonth, Double> getPurchaseHistoryPerMonth(Long storeId) {
        log.info("searching history of purchases in store with id: {}", storeId);
        List<PurchaseAction> purchases = purchaseRepository.findAllPurchasesInStore(storeId);
        return purchases.stream().collect(
                Collectors.groupingBy(p -> YearMonth.of(p.getCreatedAt().getYear(), p.getCreatedAt().getMonth()),
                        Collectors.summingDouble(PurchaseAction::getAmount)));
    }

    public Map<YearMonth, Integer> getGiftsHistoryPerMonth(Long storeId) {
        log.info("searching history of gifts in store with id: {}", storeId);
        List<OfferUseAction> purchases = offerUseRepository.findAll();
        Map<YearMonth, Integer> giftsByMonth = new HashMap<>();
        for (OfferUseAction o : purchases) {
            if (o.getOffer() instanceof Gift gift) {
                if (gift.getStore().getId().equals(storeId)) {
                    YearMonth month = YearMonth.from(o.getCreatedAt());
                    giftsByMonth.put(month, giftsByMonth.getOrDefault(month, 0) + 1);
                }
            }
        }
        return giftsByMonth;
    }

}
