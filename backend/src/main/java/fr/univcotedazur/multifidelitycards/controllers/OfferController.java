package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.components.GeographicZoneService;
import fr.univcotedazur.multifidelitycards.components.OfferHandlerService;

import fr.univcotedazur.multifidelitycards.controllers.dto.*;
import fr.univcotedazur.multifidelitycards.controllers.mapper.GiftMapper;
import fr.univcotedazur.multifidelitycards.controllers.mapper.OfferMapper;
import fr.univcotedazur.multifidelitycards.controllers.mapper.ParkingMapper;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * OfferController responsible of operations of the offers
 */
@RestController
@RequestMapping(path = OfferController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class OfferController {

    public static final String BASE_URI = "/offers";
    @Autowired
    private OfferHandlerService offerService;
    @Autowired
    private GeographicZoneService geographicZoneService;
    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private GiftMapper giftMapper;

    @PostMapping(path = {"zone/{zoneId}"}, consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectiveOfferDTO> addCollectiveOffer(@PathVariable("zoneId") Long zoneId,
            @RequestBody @Valid ParkingOfferDTO offerDTO) throws GeographicZoneNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).
                    body(parkingMapper.toDto(offerService.createParkingOffer(
                    offerDTO.getName(),offerDTO.getDescription(), offerDTO.getPoints(), offerDTO.getMinutes(),
                zoneId, offerDTO.getType())));
    }


    @PostMapping(path = {"store/{storeId}"}, consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<GiftDTO> addGift(@PathVariable("storeId") Long storeId,
            @RequestBody @Valid GiftDTO offerDTO) throws StoreNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(giftMapper.toDto((offerService.createGift(offerDTO.getName(),offerDTO.getDescription(),
                                    offerDTO.getPoints(),storeId,offerDTO.getType()))));
    }

    // explore offer getmapping
    @GetMapping(path ={ "/zone/{zoneId}"})
    public ResponseEntity<List<OfferDTO>> exploreOffer(@PathVariable("zoneId") Long zoneId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    offerMapper.toDtos(offerService.exploreOffersByGeographicZone(zoneId)));
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("gift")
    public ResponseEntity<List<GiftDTO>> exploreAllGit() {
            return ResponseEntity.status(HttpStatus.OK).body(
                    giftMapper.toDtos(offerService.exploreAllGifts()));
    }

    @GetMapping("collective")
    public ResponseEntity<List<ParkingOfferDTO>> exploreAllParking() {
        return ResponseEntity.status(HttpStatus.OK).body(
                parkingMapper.toDtos(offerService.exploreAllCollectiveOffers()));
    }

}
