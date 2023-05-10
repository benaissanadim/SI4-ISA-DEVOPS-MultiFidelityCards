package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalAdvice for messages when having exceptions
 */
@RestControllerAdvice(assignableTypes = { StoreController.class, OfferController.class, FidelityCardController.class})
public class GlobalControllerAdvice {

    @ExceptionHandler({ GeographicZoneNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(GeographicZoneNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("geographic zone not found");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ ClientNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(ClientNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("client not found");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ StoreNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(StoreNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("store not found");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }


    @ExceptionHandler({ AlreadyExistingFidelityCardException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(AlreadyExistingFidelityCardException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("already existing fidelity card");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ InvalidPaymentException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(InvalidPaymentException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("invalid payment");
        errorDTO.setError(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ NotEligibleClientException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(NotEligibleClientException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("not eligible client");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ NotEnoughPointsException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(NotEnoughPointsException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("not enough points exception");
        errorDTO.setError(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ ParkingProxyException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleExceptions(ParkingProxyException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("parking proxy error");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }
    
}
