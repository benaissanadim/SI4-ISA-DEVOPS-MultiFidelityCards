package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.CliContextStore;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextZone;
import java.util.Collections;

import fr.univcotedazur.multifidelitycard.cli.model.KPIsCA;
import fr.univcotedazur.multifidelitycard.cli.model.KPIsOffers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@ShellComponent
public class KPIsCommands {

    public static final String BASE_URI = "/KPIs/";


    @Autowired RestTemplate restTemplate;

    @Autowired
    CliContextStore cliContextStore;

    @ShellMethod("Get KPIs Store Gifts (STORE_NAME) ")
    public String getKPIsStoreGifts(String storeName) {
        try {
            ResponseEntity<List<KPIsOffers>> responseEntity = restTemplate.exchange(
                    BASE_URI + "store/" + getIdForStore(storeName) + "/gifts",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<KPIsOffers>>(){}
            );
            String s ="******************** LISTING KPIs "+storeName+" Gifts ********************";
            for(KPIsOffers kpi : responseEntity.getBody()) {
                s += "\n\t"+kpi.toString();
            }
            return s;

        } catch (Exception e) {
            log.warn("Store name {} not found", storeName);
            return "Store name not found";
        }
    }

    @ShellMethod("Get KPIs Store Purchases (STORE_NAME) ")
    public String KpiSorePurchases(String storeName) {
        try {
            ResponseEntity<List<KPIsCA>> responseEntity = restTemplate.exchange(
                    BASE_URI + "store/" + getIdForStore(storeName) + "/ca", HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<KPIsCA>>() {
                    });
            String s ="******************** LISTING KPIs "+storeName+" CA ********************";
            for(KPIsCA kpi : responseEntity.getBody()) {
                s += "\n\t"+kpi.toString();
            }
            return s;
        } catch (Exception e) {
            log.warn("Store name {} not found", storeName);
            return "Store name not found";
        }
    }

    private Long getIdForStore(String name) {
        return cliContextStore.getStoreMap().get(name).getId();
    }

}
