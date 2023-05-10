package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.components.ClientRegistryService;
import fr.univcotedazur.multifidelitycards.controllers.dto.ClientDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.mapper.ClientMapper;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingClientException;
import fr.univcotedazur.multifidelitycards.scheduler.ClientVFPScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ClientController responsible for client registry
 */
@RestController
@RequestMapping(path = ClientController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ClientController {
    public static final String BASE_URI = "/clients";

    @Autowired
    private ClientRegistryService registry;
    @Autowired
    private ClientMapper clientMapper;


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Cannot process Customer information");
        errorDTO.setDetails(ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", ")));
        return errorDTO;
    }

    @ExceptionHandler({ AlreadyExistingClientException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(AlreadyExistingClientException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        return errorDTO;
    }


    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<ClientDTO> register(@RequestBody @Valid ClientDTO cusdto)
            throws AlreadyExistingClientException {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(clientMapper.toDto(registry.register(cusdto.getUsername(), cusdto.getEmail())));

    }

    @GetMapping()
    public ResponseEntity<List<ClientDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toDtos(registry.findAll()));
    }

}