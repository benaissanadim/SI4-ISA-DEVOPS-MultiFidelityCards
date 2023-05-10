package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.*;
import fr.univcotedazur.multifidelitycard.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ShellComponent
public class InitCommand {

    @Autowired RestTemplate restTemplate;

    @Autowired
    private CliContextFidelityCard cliContextCard;
    @Autowired
    private CliContextZone cliContextZone;
    @Autowired
    private CliContextStore cliContextStore;
    @Autowired
    private CliClientContext cliContext;
    @Autowired
    private CliContextGift cliContextGift;
    @Autowired
    private CliContextCollectiveOffer cliContextCollectiveOffer;



    @ShellMethod("init")
    public void init() {
        ResponseEntity<List<CliClient>> responseEntityClients = restTemplate.exchange(
                ClientCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CliClient>>() {
                });
        for(CliClient cliClient : responseEntityClients.getBody()) {
            cliContext.getCustomers().put(cliClient.getUsername(), cliClient);
        }
        ResponseEntity<List<CliFidelityCard>> responseEntityCards = restTemplate.exchange(
                FidelityCardCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CliFidelityCard>>() {
                });
        for(CliFidelityCard cliClient : responseEntityCards.getBody()) {
            cliContextCard.getFidelityCard().put(cliClient.getNumber(), cliClient);
        }
        ResponseEntity<List<CliZoneGeographic>> responseEntityZones = restTemplate.exchange(
                ZoneGeograpicCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CliZoneGeographic>>() {
                });
        for(CliZoneGeographic cliClient : responseEntityZones.getBody()) {
            cliContextZone.getZoneGeographic().put(cliClient.getName(), cliClient);
        }
        ResponseEntity<List<CliStore>> responseEntityStores = restTemplate.exchange(
                StoreCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        for(CliStore cliClient : responseEntityStores.getBody()) {
            cliContextStore.getStoreMap().put(cliClient.getName(), cliClient);
        }

        ResponseEntity<List<CliGift>> responseEntityGift = restTemplate.exchange(
                OfferCommands.BASE_URI+"/gift", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        for(CliGift cliClient : responseEntityGift.getBody()) {
            cliContextGift.getGift().put(cliClient.getName(), cliClient);
        }

        ResponseEntity<List<CliParkingOffer>> responseEntityOffer = restTemplate.exchange(
                OfferCommands.BASE_URI+"/collective", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        for(CliParkingOffer cliClient : responseEntityOffer.getBody()) {
            cliContextCollectiveOffer.getOffer().put(cliClient.getName(), cliClient);
        }

    }

}
