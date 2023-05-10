package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.BankProxy;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.*;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardPaymentUse;
import fr.univcotedazur.multifidelitycards.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class FidelityCardServiceTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FidelityCardPaymentUse fidelityCardUse;
    @Autowired
    private FidelityCardRepository fidelityCardRepository;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;
    @Autowired
    private PurchaseRepository transactionRepository;
    @Autowired
    private StoreRepository storeRepository;
    private Client client;
    private GeographicZone zone;
    private FidelityCard fidelityCard;
    private Store store;
    @MockBean
    private BankProxy bank;

    @BeforeEach
    void setUp() {
        store = new Store();
        storeRepository.save(store);
        this.zone = new GeographicZone("antibes");
        this.geographicZoneRepository.save(this.zone);
        this.client = new Client("john", "john@gmail.com");
        clientRepository.save(client);
        System.out.println("testinnng");

        fidelityCard = new FidelityCard(this.client, this.zone);
        fidelityCard.setAmount(20.0);
        fidelityCardRepository.save(fidelityCard);
    }

    @Test
    public void buyProduct() throws Exception {
        this.fidelityCardUse.pay(fidelityCard.getId(), 10.0, store.getName());
        List<PurchaseAction> list = transactionRepository.findAll();
        assertEquals( 1, list.size());
        PurchaseAction p = list.get(0);
        assertEquals(10.0,p.getAmount());
        assertEquals(p.getFidelityCard(), fidelityCard);
        assertEquals(10.0,fidelityCard.getAmount() );
        assertEquals(1,fidelityCard.getPoints());
    }

    @Test
    public void buyProductFailed() throws Exception {
        Assertions.assertThrows(InvalidPaymentException.class, () -> {
            this.fidelityCardUse.pay(fidelityCard.getId(), 30.0, store.getName());
        });
        assertEquals(0,transactionRepository.count());
    }

    @Test
    public void addAmount() throws Exception {
        when(bank.pay("123456789",10.0)).thenReturn(true);
        this.fidelityCardUse.recharge(fidelityCard.getId(), 10.0,"123456789");
        assertEquals(30.0,fidelityCard.getAmount());
    }

    @Test
    public void addAmountFailed() throws Exception {
        when(bank.pay("123456789",10.0)).thenReturn(false);
        Assertions.assertThrows(InvalidPaymentException.class, () -> {
            this.fidelityCardUse.recharge(fidelityCard.getId(), 10.0,"123456789");
        });
    }

}
