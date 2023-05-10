package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.PurchaseAction;
import fr.univcotedazur.multifidelitycards.entities.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private FidelityCardRepository fidelityCardRepository;
    @Autowired
    private ClientRepository clientRepository;
    private PurchaseAction p1;
    private PurchaseAction p2;
    private PurchaseAction p3;
    private PurchaseAction p4;
    private PurchaseAction p5;
    private Store store1;
    private Store store2;
    private FidelityCard fidelityCard1;
    private FidelityCard fidelityCard2;

    @BeforeEach
    public void setUp()  {

        store1 = new Store();
        store1.setName("store1");
        store1.setAddress("address1");
        storeRepository.save(store1);

        store2 = new Store();
        store2.setName("store2");
        store2.setAddress("address2");
        storeRepository.save(store2);


        Client client1 = new Client();
        client1.setUsername("client1");
        clientRepository.save(client1);
        fidelityCard1 = new FidelityCard();
        fidelityCard1.setClient(client1);
        fidelityCardRepository.save(fidelityCard1);

        Client client2 = new Client();
        client2.setUsername("client2");
        clientRepository.save(client2);
        fidelityCard2 = new FidelityCard();
        fidelityCard2.setClient(client2);
        fidelityCardRepository.save(fidelityCard2);

        p1 = new PurchaseAction();
        p1.setStore(store1);
        p1.setFidelityCard(fidelityCard1);
        p1.setCreatedAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        purchaseRepository.save(p1);

        p2 = new PurchaseAction();
        p2.setStore(store1);
        p2.setFidelityCard(fidelityCard1);
        p2.setCreatedAt(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
        purchaseRepository.save(p2);

        p3 = new PurchaseAction();
        p3.setStore(store1);
        p3.setFidelityCard(fidelityCard1);
        p3.setCreatedAt(LocalDateTime.now().minus(5, ChronoUnit.DAYS));
        purchaseRepository.save(p3);

        p4 = new PurchaseAction();
        p4.setStore(store1);
        p4.setFidelityCard(fidelityCard1);
        p4.setCreatedAt(LocalDateTime.now().minus(10, ChronoUnit.DAYS));
        purchaseRepository.save(p4);

        p5 = new PurchaseAction();
        p5.setStore(store2);
        p5.setFidelityCard(fidelityCard2);
        p5.setCreatedAt(LocalDateTime.now().minus(5, ChronoUnit.DAYS));

    }

    @Test
    public void testFindCardPurchasesInStore(){
        List<PurchaseAction> purchases = purchaseRepository.findCardPurchasesInStore(fidelityCard1, store1);
        assertThat(purchases).containsExactly(p1, p2, p3, p4);

    }

    @Test
    public void testFindCardPurchasesLastWeek(){
        List<PurchaseAction> purchases = purchaseRepository.findPurchasesByFidelityCardAfter(fidelityCard1, LocalDateTime.now().minusWeeks(1));
        assertThat(purchases).containsExactly(p1, p2, p3);

    }
}
