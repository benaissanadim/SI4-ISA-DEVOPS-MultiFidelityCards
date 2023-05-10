package fr.univcotedazur.multifidelitycards.cucumber.fidelitycard;

import fr.univcotedazur.multifidelitycards.components.FidelityCardUseOffers;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.repositories.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
public class FidelityCardUseOffersTest {

    private Client client;
    private FidelityCard fidelityCard;
    ParkingOffer parkingOffer1;
    private Store store;
    @Autowired private ClientRepository clientRepository;
    @Autowired private FidelityCardRepository fidelityCardRepository;
    @Autowired private FidelityCardUseOffers fidelityCardUseOffersTest;
    @Autowired private OfferRepository offerRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private PurchaseRepository purchaseRepository;
    private String msg;
    private Gift gift;

    @Given("client {string} with email {string} with satus {string} having fidelitycard number {string} with {int} points")
    public void clientWithEmailWithSatusHavingFidelitycardNumberWithPoints(
            String arg0, String arg1, String arg2, String arg3, int arg4) {
        clientRepository.deleteAll();
        fidelityCardRepository.deleteAll();
        client = new Client(arg0, arg1);
        client.setStatus(ClientStatus.valueOf(arg2));
        clientRepository.save(client);
        fidelityCard = new FidelityCard();
        fidelityCard.setNumber(arg3);
        fidelityCard.setPoints(arg4);
        fidelityCard.setClient(client);
        fidelityCardRepository.save(fidelityCard);
    }


    @When("{string} uses parking1")
    public void usesParking(String arg0) {
        try {
            fidelityCardUseOffersTest.useParking(parkingOffer1.getId(), fidelityCard.getId());
        } catch (Exception e) {
            msg = e.getMessage();
        }
    }

    @Given("parking {string} in {string} with points {int}  {string}")
    public void parkingInWithPoints(String arg0, String arg1, int arg2, String arg3) {
        parkingOffer1 = new ParkingOffer();
        parkingOffer1.setPoints(arg2);
        parkingOffer1.setName(arg0);
        parkingOffer1.setType(OfferType.valueOf(arg3));
        offerRepository.save(parkingOffer1);
    }


    @Then("exception {string} is thrown")
    public void exceptionIsThrown(String arg0) {

        assertThat(msg).isEqualTo(arg0);
    }

    @Given("client having no purchases in store {string}") public void clientHavingNoPurchasesInStore(String arg0) {
    }

    @And("store {string}")
    public void store(String arg0) {
        store = new Store();
        store.setName(arg0);
        storeRepository.save(store);
    }

    @And("gift {string} in {string} with points {int}  {string}")
    public void giftInWithPoints(String arg0, String arg1, int arg2, String arg3) {
        gift = new Gift();
        gift.setPoints(arg2);
        gift.setName(arg0);
        System.out.println("OMAR");
        System.out.println(store);
        gift.setStore(store);
        gift.setType(OfferType.valueOf(arg3));
        offerRepository.save(gift);
    }

    @When("{string} try to use gift")
    public void tryToUseGift(String arg0) {

        try {
           fidelityCard = fidelityCardUseOffersTest.receiveGift(gift.getId(), fidelityCard.getId());
        } catch (Exception e) {
            msg = e.getMessage();
        }
    }

    @Given("client having {int} purchases in store {string}") public void clientHavingPurchasesInStore(int arg0,
            String arg1) {
        System.out.println("HAMZA");
        System.out.println(store);
        System.out.println("ok");
        System.out.println(fidelityCard);
        PurchaseAction p1 = new PurchaseAction();
        p1.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        p1.setFidelityCard(fidelityCard);
        p1.setStore(store);
        p1.setTransactionType(TransactionType.PURCHASE);
        purchaseRepository.save(p1);
        PurchaseAction p = new PurchaseAction();
        p.setCreatedAt(LocalDateTime.now().minusMinutes(60));
        p.setFidelityCard(fidelityCard);
        p.setStore(store);
        p.setTransactionType(TransactionType.PURCHASE);
        purchaseRepository.save(p);
    }

    @Then("fidelity card has points {int}")
    public void fidelityCardHasPoints(int arg0) {
        System.out.println(fidelityCardRepository.findAll());
        assertThat(fidelityCard.getPoints()).isEqualTo(arg0);
    }

}