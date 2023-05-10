package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.BankProxy;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.FidelityCardNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.InvalidPaymentException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardPaymentUse;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FidelityCardPayment to pay and recharge fidelity card
 */
@Transactional
@Component
public class FidelityCardPayment implements FidelityCardPaymentUse {

    Logger log = LoggerFactory.getLogger(FidelityCardPayment.class);

    private final FidelityCardHandlerService fidelityCardHandlerService;
    private final IStoreFinder storeFinder;
    private final HistoryService clientHistory;
    private final BankProxy bank;

    @Autowired
    public FidelityCardPayment(FidelityCardHandlerService fidelityCardHandlerService, IStoreFinder storeFinder,
            HistoryService clientHistory, BankProxy bank) {
        this.fidelityCardHandlerService = fidelityCardHandlerService;
        this.storeFinder = storeFinder;
        this.clientHistory = clientHistory;
        this.bank = bank;
    }

    public FidelityCard pay(Long fidelityCardId, double amount, String name)
            throws FidelityCardNotFoundException, InvalidPaymentException, StoreNotFoundException {
        log.debug("paying with fidelity card {} for amount {} in store {}",
                fidelityCardId, amount, name);
        FidelityCard fidelityCard = fidelityCardHandlerService.findById(fidelityCardId);
        Store store = storeFinder.findByName(name);
        this.fidelityCardHandlerService.retreiveAmount(fidelityCard, amount);
        this.clientHistory.addPurchaseToHistory(fidelityCard,amount, store);
        this.fidelityCardHandlerService.addPointsFromAmount(fidelityCard, amount);
        log.info("paying with fidelity card {} for amount {} in store {} with SUCCESS",
                fidelityCardId, amount, store);
        return fidelityCard;
    }

    public FidelityCard recharge(Long fidelityCardId, double amount, String creditCard)
            throws FidelityCardNotFoundException, InvalidPaymentException {
        FidelityCard fidelityCard = fidelityCardHandlerService.findById(fidelityCardId);
        if(!bank.pay(creditCard, amount)){
            throw new InvalidPaymentException("Invalid payment when calling bank");
        }
        this.fidelityCardHandlerService.addAmount(fidelityCard, amount);
        log.info("recharge with fidelity card {} for amount {} with SUCCESS",
                fidelityCardId, amount);
        return fidelityCard;
    }
}
