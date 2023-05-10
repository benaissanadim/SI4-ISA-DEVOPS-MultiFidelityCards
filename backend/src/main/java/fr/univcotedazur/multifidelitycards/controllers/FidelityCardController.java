package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.PaymentDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.FidelityCardDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.PurchaseDTO;
import fr.univcotedazur.multifidelitycards.controllers.mapper.FidelityCardMapper;
import fr.univcotedazur.multifidelitycards.exceptions.*;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardCreator;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardFinder;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardPaymentUse;
import fr.univcotedazur.multifidelitycards.interfaces.IFidelityCardOfferUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * FidelityCardController responsible for card operations
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class FidelityCardController {
    public static final String CLIENT_CARD_URI = ClientController.BASE_URI+"/{clientId}/fidelityCard";
    public static final String CARD_URI = "/fidelityCards/{fidelityCardId}";

    @Autowired
    private FidelityCardPaymentUse fidelityCardUse;
    @Autowired
    private FidelityCardCreator fidelityCardCreator;
    @Autowired
    private IFidelityCardOfferUse fidelityCardOfferUse;
    @Autowired
    private FidelityCardMapper fidelityCardMapper;
    @Autowired
    private FidelityCardFinder fidelityCardFinder;

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Cannot process Fidelity Card action");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ FidelityCardNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(FidelityCardNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        return errorDTO;
    }

    @PostMapping(CLIENT_CARD_URI)
    public ResponseEntity<FidelityCardDTO> createClientFidelityCard(@PathVariable("clientId")
            Long clientId, @RequestBody @Valid FidelityCardDTO dto)
            throws ClientNotFoundException, AlreadyExistingFidelityCardException, GeographicZoneNotFoundException {
            return ResponseEntity.status(HttpStatus.CREATED).body(fidelityCardMapper.toDto(
                    fidelityCardCreator.create(clientId, dto.getGeographicZone())));
    }

    @PostMapping(path = CARD_URI+"/pay")
    public ResponseEntity<FidelityCardDTO> buyProduct(@PathVariable("fidelityCardId") Long fidelityCardId,
            @RequestBody @Valid PurchaseDTO dto)
            throws FidelityCardNotFoundException, InvalidPaymentException, StoreNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(fidelityCardMapper.toDto(
                fidelityCardUse.pay(fidelityCardId, dto.getAmount(),dto.getStore())));

    }

    @PostMapping(path = CARD_URI+"/recharge")
    public ResponseEntity<FidelityCardDTO> addAmountToFidelityCard(@PathVariable("fidelityCardId") Long fidelityCardId,
            @RequestBody @Valid PaymentDTO dto) throws FidelityCardNotFoundException, InvalidPaymentException {
        return ResponseEntity.status(HttpStatus.CREATED).body(fidelityCardMapper.toDto(
                    fidelityCardUse.recharge(fidelityCardId, dto.getAmount(), dto.getCreditCard())));
    }

    @PostMapping(path = CARD_URI+"/gift/{giftId}")
    public ResponseEntity<FidelityCardDTO> receiveGift(@PathVariable("fidelityCardId") Long fidelityCardId,
            @PathVariable("giftId") Long giftId)
            throws FidelityCardNotFoundException, NotEnoughPointsException, NotEligibleClientException,
            OfferNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(fidelityCardMapper.toDto(
                    fidelityCardOfferUse.receiveGift(giftId,fidelityCardId)));
    }

    @PostMapping(path = CARD_URI+"/parking/{parkingId}")
    public ResponseEntity<FidelityCardDTO> useParking(@PathVariable("fidelityCardId") Long fidelityCardId,
            @PathVariable("parkingId") Long parkingId)
            throws FidelityCardNotFoundException, NotEnoughPointsException, NotEligibleClientException,
            OfferNotFoundException, ParkingProxyException {
        return ResponseEntity.status(HttpStatus.OK).body(fidelityCardMapper.toDto(
                fidelityCardOfferUse.useParking(parkingId,fidelityCardId)));
    }

    @GetMapping("fidelityCards")
    public ResponseEntity<List<FidelityCardDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(fidelityCardMapper.toDtos(
                fidelityCardFinder.findAll()));
    }

}