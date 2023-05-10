package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.PurchaseAction;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.repositories.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClientHistoryTest {
    //tests
    PurchaseAction p1,p2,p3,p4,p5,p6,p7;
    @MockBean
    private PurchaseRepository purchaseRepository;
    @Autowired
    private HistoryService clientHistory;

    @BeforeEach
    public void setUp() {
        //
        p1 = new PurchaseAction();
        p1.setCreatedAt(LocalDateTime.now().minusDays(1));
        p2 = new PurchaseAction();
        p2.setCreatedAt(LocalDateTime.now().minusDays(2));
        p3 = new PurchaseAction();
        p3.setCreatedAt(LocalDateTime.now().minusDays(3));
        p4 = new PurchaseAction();
        p4.setCreatedAt(LocalDateTime.now().minusDays(4));
        p5 = new PurchaseAction();
        p5.setCreatedAt(LocalDateTime.now().minusDays(5));
        p6 = new PurchaseAction();
        p6.setCreatedAt(LocalDateTime.now().minusDays(6));
        p7 = new PurchaseAction();
        p7.setCreatedAt(LocalDateTime.now().minusDays(7));
    }

    @Test
    public void testFindNumberPurchasesLastWeekPerDay() {
        List<PurchaseAction> purchases = List.of(p1,p1,p1,p2,p2,p2,p3,p3,p4,p4,p5,p5,p5,p6,p6,p7);
        when(purchaseRepository.findPurchasesByFidelityCardAfter(any(FidelityCard.class),
                any(LocalDateTime.class))).thenReturn(purchases);
        FidelityCard fidelityCard = new FidelityCard();
        fidelityCard.setClient(new Client("nadim", "nadim"));
        Map<LocalDate, Long> result = clientHistory.findNumberPurchasesLastWeekPerDay(fidelityCard,
               LocalDateTime.now());
        HashMap<LocalDate, Long> map = new HashMap<>();
        map.put(LocalDate.now().minusDays(1), 3L);
        map.put(LocalDate.now().minusDays(2), 3L);
        map.put(LocalDate.now().minusDays(3), 2L);
        map.put(LocalDate.now().minusDays(4), 2L);
        map.put(LocalDate.now().minusDays(5), 3L);
        map.put(LocalDate.now().minusDays(6), 2L);
        map.put(LocalDate.now().minusDays(7), 1L);
        assertEquals(map, result);
    }

    @Test
    public void testFindPurchasesInStore() {
        List<PurchaseAction> purchases = List.of(p1,p1,p1,p2,p2,p2,p3,p3,p4,p4,p5,p5,p5,p6,p6,p7);
        when(purchaseRepository.findCardPurchasesInStore(any(FidelityCard.class),
                any(Store.class))).thenReturn(purchases);
        List<PurchaseAction> result = clientHistory.findPurchasesInStore(new Store(), new FidelityCard());
        assertEquals(purchases, result);
    }
}
