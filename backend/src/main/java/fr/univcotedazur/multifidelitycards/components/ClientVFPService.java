
package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.NotifierProxy;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.ClientStatus;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.interfaces.ClientStatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * ClientVFPService responsible for status vfp
 */
@Transactional
@Component
public class ClientVFPService implements ClientStatusManager {

    Logger log = LoggerFactory.getLogger(ClientVFPService.class);

    private final HistoryService clientHistoryPurchase;
    private final NotifierProxy notifierProxy;

    private static final int PURCHASES_PER_DAY = 1;

    @Autowired
    public ClientVFPService(HistoryService clientHistoryPurchase, NotifierProxy notifierProxy) {
        this.clientHistoryPurchase = clientHistoryPurchase;
        this.notifierProxy = notifierProxy;
    }

    public void changeStatus(Client client) {
        FidelityCard card = client.getFidelityCard();
        Map<LocalDate, Long> purchaseCounts = clientHistoryPurchase.
                findNumberPurchasesLastWeekPerDay(card, LocalDateTime.now());
        boolean verify = !purchaseCounts.isEmpty() &&
                purchaseCounts.values().stream().allMatch(c -> c >= PURCHASES_PER_DAY);
        if(verify && client.getStatus() == ClientStatus.NORMAL) {
            notifierProxy.notifyVFP(client);
            client.setStatus(ClientStatus.VFP);
        } else if(!verify && client.getStatus() == ClientStatus.VFP) {
            notifierProxy.notifyNotVFP(client);
            client.setStatus(ClientStatus.NORMAL);
        }
        log.info("Client {} status is {}", client.getUsername(), client.getStatus());
    }

}
