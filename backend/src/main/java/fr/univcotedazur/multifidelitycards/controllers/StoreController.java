package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.components.ClientRegistryService;
import fr.univcotedazur.multifidelitycards.components.StoreService;
import fr.univcotedazur.multifidelitycards.controllers.dto.ClientDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ScheduleDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.StoreDTO;
import fr.univcotedazur.multifidelitycards.controllers.mapper.ClientMapper;
import fr.univcotedazur.multifidelitycards.controllers.mapper.StoreMapper;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * StoreController responsible for store operations
 */
@RestController
@RequestMapping(path = StoreController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class StoreController {
    public static final String BASE_URI = "/stores";

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreMapper mapper;
    @Autowired
    private ClientRegistryService clientService;
    @Autowired ClientMapper clientMapper;

    // The 422 (Unprocessable Entity) status code means the server understands the content type of the request entity
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)

    @GetMapping(path = "/{storeId}")
    public ResponseEntity<StoreDTO> getStore(@PathVariable("storeId") Long storeId) {
        try {
        Store store = storeService.findById(storeId);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(store));
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping( consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<StoreDTO> register(@RequestBody @Valid StoreDTO storeDTO)
            throws GeographicZoneNotFoundException {
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(mapper.toDto(storeService.register(storeDTO.getName(), storeDTO.getAddress(),
                    storeDTO.getZone())));
    }

    @PutMapping("/{storeId}/schedule")
    public ResponseEntity<StoreDTO> addSchedule(@PathVariable Long storeId,
            @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).
                    body(mapper.toDto(storeService.addSchedule(storeId, scheduleDTO)));
    }

    @GetMapping(path = {"/GeoZone/{zone}"})
    public ResponseEntity<List<StoreDTO>> consultStoresperZone(@PathVariable("zone") String zone) {
        return ResponseEntity.status(HttpStatus.OK).
                    body(mapper.toDtos(storeService.consultStoresperZone(zone)));
    }

    @PostMapping(path = {"/{storeId}/favourites/client/{clientId}"})
    public ResponseEntity<String> addFavoriteStore(@PathVariable("storeId") Long storeId, @PathVariable("clientId") Long clientId) {
        if(storeService.addFavoriteStore(storeId, clientId)){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("store added to favorites");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("failed to add client to favourite store");
        }
    }

    @GetMapping(path = {"/{storeId}/favourites"})
    public ResponseEntity<Set<ClientDTO>> getSubscribedClients(@PathVariable("storeId") Long storeId) {
        try {
            Store store = storeService.findById(storeId);
            Set<Client> clients = store.getClientsFavorite();
            return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toDtos(clients));
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<StoreDTO>> exploreAll() {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDtos(
                storeService.consultAll()));
    }

    @DeleteMapping(path = {"/{storeId}"})
    public ResponseEntity<String> deleteStore(@PathVariable("storeId") Long storeId) {
        storeService.deleteStore(storeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("store deleted successfully");
    }

}