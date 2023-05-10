package fr.univcotedazur.multifidelitycards.cucumber.offer;

import fr.univcotedazur.multifidelitycards.components.GeographicZoneService;
import fr.univcotedazur.multifidelitycards.components.OfferHandlerService;
import fr.univcotedazur.multifidelitycards.components.StoreService;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import fr.univcotedazur.multifidelitycards.repositories.OfferRepository;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class OfferCreateTest {

    @Autowired
    OfferHandlerService offerHandlerService;
    @Autowired
    OfferRepository offerRepository;
    @Autowired
    GeographicZoneRepository geographicZoneRepository;
    @Autowired
    GeographicZoneService geographicZoneService;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreService storeService;


    GeographicZone zone;
    GeographicZone zone2;
    Store store;
    List<Offer> offer;


    @When("a merchant adds a new gift in new zone {string}")
    public void aMerchantAddsANewGiftInNewZone(String arg0) throws AlreadyExistingGeographicZoneException, GeographicZoneNotFoundException, StoreNotFoundException {
        zone = geographicZoneService.add(arg0);
        store = storeService.register("carrefour","antibes",zone.getName());
        offerHandlerService.createGift("promo","carrefour",4,store.getId(), OfferType.CLASSIC_OFFER);
    }

    @Then("the number of gifts should increase by {int}")
    public void theNumberOfGiftsShouldIncreaseBy(int arg0) {
        Assertions.assertEquals(arg0,offerRepository.count());
    }

    @And("when a user explore offers in this zone")
    public void whenAUserExploreOffersInThisZone() {
         offer = offerHandlerService.exploreOffersByGeographicZone(zone.getId());
    }

    @Then("he should see the new gift")
    public void heShouldSeeTheNewGift() {
        Gift gift = new Gift("promo","carrefour",4,store, OfferType.CLASSIC_OFFER);
        Assertions.assertEquals(1,offer.size());
        Assertions.assertEquals(gift.getName(),offer.get(0).getName());
    }

    @When("a merchant adds a new parking offer in new zone {string}")
    public void aMerchantAddsANewParkingOfferInNewZone(String arg0) throws AlreadyExistingGeographicZoneException, GeographicZoneNotFoundException, StoreNotFoundException {
        zone2 = geographicZoneService.add(arg0);
        offerHandlerService.createParkingOffer("parking","carrefour",4,15,zone2.getId(), OfferType.CLASSIC_OFFER);
    }

    @Then("the number of parking offers should increase by {int}")
    public void theNumberOfParkingOffersShouldIncreaseBy(int arg0) {
        Assertions.assertEquals(2,offerRepository.count());

    }

    @And("when a user explore offers in {string}")
    public void whenAUserExploreOffersIn(String arg0) {
        offer = offerHandlerService.exploreOffersByGeographicZone(zone2.getId());

    }

    @Then("he should see the new parking offer")
    public void heShouldSeeTheNewParkingOffer() {
        ParkingOffer parkingOffer = new ParkingOffer("parking","carrefour",4,zone2,OfferType.CLASSIC_OFFER,15);
        Assertions.assertEquals(1,offer.size());
        Assertions.assertEquals(parkingOffer.getName(),offer.get(0).getName());
    }


}
