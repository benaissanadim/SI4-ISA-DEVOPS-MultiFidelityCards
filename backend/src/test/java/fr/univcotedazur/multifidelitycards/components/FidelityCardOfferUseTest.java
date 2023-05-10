package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.FidelityCardNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.NotEligibleClientException;
import fr.univcotedazur.multifidelitycards.exceptions.NotEnoughPointsException;
import fr.univcotedazur.multifidelitycards.exceptions.OfferNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.IFidelityCardOfferUse;
import fr.univcotedazur.multifidelitycards.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FidelityCardOfferUseTest {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private FidelityCardRepository fidelityCardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private OfferUseRepository offerUseRepository;
    private PurchaseAction p1;
    private PurchaseAction p2;
    private PurchaseAction p3;
    private Store store1;
    private Gift gift;
    private Client client1;
    private ParkingOffer parkingOffer;
    private FidelityCard fidelityCard1;

    @Autowired
    private IFidelityCardOfferUse fidelityCardOfferUse;

    @BeforeEach
    public void setUp()  {
        offerRepository.deleteAll();
        clientRepository.deleteAll();
        store1 = new Store();
        store1.setName("store1");
        store1.setAddress("address1");
        storeRepository.save(store1);
        client1 = new Client();
        client1.setUsername("client1"+ UUID.randomUUID());
        clientRepository.save(client1);
        fidelityCard1 = new FidelityCard();
        fidelityCard1.setTransactions(new ArrayList<>());
        fidelityCard1.setClient(client1);
        fidelityCardRepository.save(fidelityCard1);
        p1 = new PurchaseAction();
        p1.setStore(store1);
        p1.setFidelityCard(fidelityCard1);
        p1.setCreatedAt(LocalDateTime.now());
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
        gift = new Gift();
        gift.setStore(store1);
        gift.setPoints(10);
        gift.setType(OfferType.CLASSIC_OFFER);
        offerRepository.save(gift);
        parkingOffer = new ParkingOffer();
        parkingOffer.setName("paarking");
        parkingOffer.setPoints(50);
        parkingOffer.setType(OfferType.CLASSIC_OFFER);
        offerRepository.save(parkingOffer);
    }

    @Test
    public void testReceiveGift()
            throws FidelityCardNotFoundException, NotEnoughPointsException, NotEligibleClientException,
            OfferNotFoundException {
        fidelityCard1.setPoints(25);
        fidelityCardRepository.save(fidelityCard1);
        fidelityCard1 = fidelityCardOfferUse.receiveGift(gift.getId(), fidelityCard1.getId());
        assertThat(fidelityCard1.getPoints()).isEqualTo(15);
        assertThat(offerRepository.count()).isEqualTo(2);
        assertThat(offerUseRepository.findAll()).containsExactly(new OfferUseAction(fidelityCard1, gift));
    }

    @Test
    public void testReceiveGiftNotEnoughPoints() {
        fidelityCard1.setPoints(5);
        fidelityCardRepository.save(fidelityCard1);
        Assertions.assertThrows(NotEnoughPointsException.class, () -> {
            fidelityCardOfferUse.receiveGift(gift.getId(), fidelityCard1.getId());
        });
    }

    @Test
    public void testReceiveGiftNotFound() {
        Assertions.assertThrows(OfferNotFoundException.class, () -> {
            fidelityCardOfferUse.receiveGift(100L, fidelityCard1.getId());
        });
    }

    @Test
    public void testReceiveCardNotFound() {
        Assertions.assertThrows(FidelityCardNotFoundException.class, () -> {
            fidelityCardOfferUse.receiveGift(gift.getId(), 100L);
        });
    }

    @Test
    public void useParkingKOOfferFound() {
        Assertions.assertThrows(OfferNotFoundException.class, () -> {
            fidelityCardOfferUse.useParking(100L, 100L);
        });
    }

    @Test
    public void useParkingKOFidelityCardNotFound() {
        Assertions.assertThrows(FidelityCardNotFoundException.class, () -> {
            fidelityCardOfferUse.useParking(parkingOffer.getId(), 100L);
        });
    }

    @Test
    public void useParkingKONotEligible() {
        Assertions.assertThrows(NotEnoughPointsException.class, () -> {
            fidelityCardOfferUse.useParking(parkingOffer.getId(), fidelityCard1.getId());
        });
    }

    @Test
    public void useParkingKONotEligible2() {
        parkingOffer.setType(OfferType.VFP_OFFER);
        offerRepository.save(parkingOffer);
        client1.setStatus(ClientStatus.NORMAL);
        Assertions.assertThrows(NotEligibleClientException.class, () -> {
            fidelityCardOfferUse.useParking(parkingOffer.getId(), fidelityCard1.getId());
        });
    }





}
