package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.GeographicZoneDTO;
import fr.univcotedazur.multifidelitycards.controllers.mapper.GeographicZoneMapper;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneAdder;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * GeographicZoneController responsible for adding new zones
 */
@RestController
@RequestMapping(path = GeographicZoneController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class GeographicZoneController {
    public static final String BASE_URI = "/geographicZones";

    @Autowired
    private GeographicZoneAdder geographicZoneAdder;
    @Autowired
    private GeographicZoneMapper geographicZoneMapper;
    @Autowired
    private GeographicZoneFinder geographicZoneFinder;

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Cannot process Geographic Zone information");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler({ AlreadyExistingGeographicZoneException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(AlreadyExistingGeographicZoneException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        return errorDTO;
    }

    @PostMapping( consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GeographicZoneDTO> add(@RequestBody @Valid GeographicZoneDTO dto)
            throws AlreadyExistingGeographicZoneException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                    geographicZoneMapper.toDto(geographicZoneAdder.add(dto.getName())));

    }

    @GetMapping()
    public ResponseEntity<List<GeographicZoneDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(geographicZoneMapper.toDtos(
                geographicZoneFinder.findAll()));
    }

}
