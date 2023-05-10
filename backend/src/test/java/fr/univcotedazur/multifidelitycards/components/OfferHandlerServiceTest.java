package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.OfferNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.*;
import fr.univcotedazur.multifidelitycards.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OfferHandlerServiceTest {

    @Autowired
    private IOfferManager offerManager;
    @Autowired
    private IOfferExplorator offerExplorator;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;

    private Offer offer;
    private Gift gift;
    private CollectiveOffer collectiveOffer;
    private Store store;
    private GeographicZone geographicZone;



    @BeforeEach
    void setUp() {
        this.offerRepository.deleteAll();
    }

    @Test
    void deleteOffer() throws  OfferNotFoundException {
        this.offer = new Offer("offer","offer", 1, OfferType.CLASSIC_OFFER);
        offerRepository.save(this.offer);
        Assertions.assertEquals(1, this.offerRepository.count());
        this.offerManager.deleteOffer(offer.getId());
        Assertions.assertEquals(0, this.offerRepository.count());
    }

    @Test
    void deleteOfferWithNonExistingOffer() {
        this.offer = new Offer("offer","offer", 1, OfferType.CLASSIC_OFFER);
        offer.setId(1L);
        offerRepository.save(this.offer);
        Assertions.assertEquals(1, this.offerRepository.count());
        assertThrows(OfferNotFoundException.class, () -> {
            this.offerManager.deleteOffer(99L);
        });
        Assertions.assertEquals(1, this.offerRepository.count());
    }

    @Test
    void createGiftOffer() throws StoreNotFoundException {
        store = new Store();
        store.setGifts(new ArrayList<>());
        storeRepository.save(this.store);
        gift = offerManager.createGift("gift","gift", 1,store.getId(), OfferType.CLASSIC_OFFER);

        Assertions.assertEquals(1, this.offerRepository.count());
        Assertions.assertEquals(gift, offerRepository.findById(gift.getId()).get());
    }

    @Test
    void createGiftOfferStoreNotFound(){
        assertThrows(StoreNotFoundException.class, () -> {
            this.gift = offerManager.createGift("gift","gift", 1,1L, OfferType.CLASSIC_OFFER);
        });
        Assertions.assertEquals(0, this.offerRepository.count());
    }

    @Test
    void createCollectiveOffer() throws GeographicZoneNotFoundException {
        geographicZone = new GeographicZone("zone");
        geographicZoneRepository.save(geographicZone);
        collectiveOffer= offerManager.createParkingOffer("gift","gift", 1,30,geographicZone.getId(),
                OfferType.CLASSIC_OFFER);
        Assertions.assertEquals(1, offerRepository.count());
        Assertions.assertEquals(collectiveOffer,
                offerRepository.findById(collectiveOffer.getId()).get());
    }

    @Test
    void createCollectiveOfferNotFound() throws GeographicZoneNotFoundException {
        assertThrows(GeographicZoneNotFoundException.class, () -> {
            collectiveOffer = offerManager.createParkingOffer("gift","gift", 1,30,100L,
                    OfferType.CLASSIC_OFFER);
        });
        Assertions.assertEquals(0, this.offerRepository.count());
;
        Assertions.assertEquals(0, this.offerRepository.count());
    }

    @Test
    void exploreOffersTest(){
        GeographicZone geographicZone1 = new GeographicZone("antibes");
        geographicZoneRepository.save(geographicZone1);
        GeographicZone geographicZone2 = new GeographicZone("nice");
        geographicZoneRepository.save(geographicZone2);
        Store store1 = new Store("store", "dolines", null, geographicZone1);
        Store store2 = new Store("store2", "nice", null, geographicZone2);
        Store store3 = new Store("store3", "antibes", null, geographicZone1);
        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);
        Gift gift1 = new Gift("gift1","gift1", 1, store1, OfferType.CLASSIC_OFFER);
        Gift gift2 = new Gift("gift2","gift2", 1, store2, OfferType.CLASSIC_OFFER);
        Gift gift3 = new Gift("gift3","gift3", 1, store3, OfferType.CLASSIC_OFFER);
        offerRepository.save(gift1);
        offerRepository.save(gift2);
        offerRepository.save(gift3);
        CollectiveOffer collectiveOffer1 = new CollectiveOffer("collectiveOffer1","collectiveOffer1", 1,
                geographicZone1, OfferType.CLASSIC_OFFER);
        CollectiveOffer collectiveOffer2 = new CollectiveOffer("collectiveOffer2","collectiveOffer2", 1,geographicZone2,
        OfferType.VFP_OFFER);
        offerRepository.save(collectiveOffer1);
        offerRepository.save(collectiveOffer2);
        List<Offer> offers = offerExplorator.exploreOffersByGeographicZone(geographicZone1.getId());
        Assertions.assertEquals(List.of(gift1,gift3, collectiveOffer1), offers);
    }

}
