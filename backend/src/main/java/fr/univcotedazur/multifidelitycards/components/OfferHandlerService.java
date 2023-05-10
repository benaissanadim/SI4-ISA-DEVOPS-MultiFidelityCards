package fr.univcotedazur.multifidelitycards.components;


import fr.univcotedazur.multifidelitycards.entities.Offer;
import fr.univcotedazur.multifidelitycards.entities.OfferType;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.OfferNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.*;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.interfaces.IOfferManager;

import fr.univcotedazur.multifidelitycards.repositories.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OfferHandlerService to handle and manage the fidelity card
 */
@Transactional
@Component
public class OfferHandlerService implements IOfferManager, IOfferExplorator, IOfferFinder {

    private final OfferRepository offerRepository;
    private final GeographicZoneFinder geographicZoneFinder;
    private final IStoreFinder storeFinder;

    Logger log = LoggerFactory.getLogger(OfferHandlerService.class);

    @Autowired
    public  OfferHandlerService(OfferRepository offerRepository, GeographicZoneFinder geographicZoneFinder,
                IStoreFinder storeFinder) {
            this.offerRepository = offerRepository;
            this.geographicZoneFinder = geographicZoneFinder;
            this.storeFinder = storeFinder;
    }


    @Override
    public void deleteOffer(Long offerId) throws OfferNotFoundException {
        //
        if(offerRepository.findById(offerId).isEmpty()){
            throw new OfferNotFoundException("offer of id :" + offerId + " does not exists");
        }
        else{
            this.offerRepository.deleteById(offerId);
        }
    }

    @Override
    public Gift createGift(String name,String description, int points, Long storeId, OfferType type)
            throws StoreNotFoundException {
        Store store = storeFinder.findById(storeId);
        Gift gift = new Gift(name,description, points, store, type);
        this.offerRepository.save(gift);
        store.getGifts().add(gift);
        return gift;
    }


    @Override
    public ParkingOffer createParkingOffer(String name,String description, int points, int minutes,
            Long geographicZoneId, OfferType type)
            throws GeographicZoneNotFoundException {
        GeographicZone zone = geographicZoneFinder.findById(geographicZoneId);
        ParkingOffer parkingOffer = new ParkingOffer(name,description, points,zone, type, minutes);
        this.offerRepository.save(parkingOffer);
        log.info("create parking offer : {}",  parkingOffer);
        return parkingOffer;
    }



    @Override
    public List<Offer> exploreOffersByGeographicZone(Long geographicZoneId) {
        return offerRepository.findOfferByGeographicZoneId(geographicZoneId);
    }

    @Override
    public List<Gift> exploreAllGifts() {
        return offerRepository.findAll().stream()
                .filter(offer -> offer instanceof Gift)
                .map(offer -> (Gift) offer)
                .collect(Collectors.toList());
    }

    @Override public List<ParkingOffer> exploreAllCollectiveOffers() {
        return offerRepository.findAll().stream()
                .filter(offer -> offer instanceof ParkingOffer)
                .map(offer -> (ParkingOffer) offer)
                .collect(Collectors.toList());
    }

    @Override
    public Offer findById(Long offerId) throws OfferNotFoundException {
        return offerRepository.findById(offerId)
                .orElseThrow(() ->
                        new OfferNotFoundException("offer of id :" + offerId + " does not exists"));
    }

}
