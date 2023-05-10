package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.NotifierProxy;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.interfaces.IClientHistory;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import fr.univcotedazur.multifidelitycards.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelitycards.scheduler.ClientVFPScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ClientVFPServiceTest {


    @MockBean
    private IClientHistory clientHistoryPurchase;

    Client client;
    @MockBean
    private NotifierProxy notifierProxy;
    @Autowired
    private ClientVFPService clientVFPService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FidelityCardRepository fidelityCardRepository;
    @MockBean
    private PurchaseRepository purchaseRepository;


    @Mock
    private PurchaseAction p1, p2, p3, p4, p5, p6, p7;

    @BeforeEach
    public void setUp() {
        client = new Client("nadim", "nadim@gmail.com");
        FidelityCard card = new FidelityCard();
        fidelityCardRepository.save(card);
        client.setFidelityCard(card);
        card.setClient(client);
        clientRepository.save(client);
    }

    @Test
    public void testChangeStatusNormal() {
        when(clientHistoryPurchase.findNumberPurchasesLastWeekPerDay(any(FidelityCard.class),
                any(LocalDateTime.class))).thenReturn(new HashMap<>());
        clientVFPService.changeStatus(client);
        assertEquals( ClientStatus.NORMAL,client.getStatus());
    }

    @Test
    public void testChangeStatusVFP() {

        when(p1.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));
        when(p2.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(2));
        when(p3.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(3));
        when(p4.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(4));
        when(p5.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(5));
        when(p6.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(6));
        when(p7.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(7));

        List<PurchaseAction> purchases = List.of(p1,p1,p1,p1,p2,
                p2,p2,p2,p3,p3,p3,p4,p4,p4,p5,p5,p5,p6,p6,p6,p7,p7,p7);
        when(purchaseRepository.findPurchasesByFidelityCardAfter(any(FidelityCard.class),
                any(LocalDateTime.class))).thenReturn(purchases);
        when(notifierProxy.notifyVFP(any(Client.class))).thenReturn(true);
        clientVFPService.changeStatus(client);
        assertEquals( ClientStatus.VFP,client.getStatus());
    }

}