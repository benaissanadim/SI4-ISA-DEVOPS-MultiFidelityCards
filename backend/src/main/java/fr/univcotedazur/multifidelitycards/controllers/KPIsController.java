package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.entities.KPIsCA;
import fr.univcotedazur.multifidelitycards.entities.KPIsOffers;
import fr.univcotedazur.multifidelitycards.components.KPIsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * KPIs controller to be shown for stores
 */
@RestController
@RequestMapping(path = KPIsController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class KPIsController {

    public static final String BASE_URI = "/KPIs";

    @Autowired
    private KPIsService storeHistory;


    @GetMapping(path ={ "/store/{storeId}/gifts"})
    public ResponseEntity<List<KPIsOffers>> exploreKPIsGiftsStore(@PathVariable("storeId") Long storeId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    storeHistory.getKPIsGiftsEvolution(storeId));
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(path ={ "/store/{storeId}/ca"})
    public ResponseEntity<List<KPIsCA>> exploreKPIsPurchasesStore(@PathVariable("storeId") Long storeId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    storeHistory.getKPIsCAEvolution(storeId));
        } catch (Exception var3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}