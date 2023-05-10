package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.*;
import fr.univcotedazur.multifidelitycard.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class OfferCommands {

    public static final String BASE_URI = "/offers";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private CliContextGift cliContextGift;
    @Autowired
    private CliContextStore cliContextStore;
    @Autowired
    private CliContextCollectiveOffer cliContextCollectiveOffer;
    @Autowired
    private CliContextOffer cliContextOffer;
    @Autowired
    private CliContextZone contextZone;


    @ShellMethod("add gift (NAME DESCRIPTION POINTS STORE_NAME TYPE(CLASSIC_OFFER OR VFP_OFFER) )")
    public CliGift addGift(String name,String description, int points, String store , String type ) {
        CliGift res = restTemplate.postForObject(getUriForStoreGift(store), new CliGift(name,description,points,store,type), CliGift.class);
        cliContextGift.getGift().put(res.getName(), res);
        return res;
    }


    private String getUriForStoreGift(String name) {
        return BASE_URI +"/store/"+ cliContextStore.getStoreMap().get(name).getId() + "/";
    }

    @ShellMethod("add parking offer in the CoD (NAME DESCRIPTION POINTS GEOGRAPHIC ZONE_NAME TYPE MINUTES)")
    public CliParkingOffer addCollectiveOffer(String name,String description, int points, String geoZone, String type,
            int minutes) {
        CliParkingOffer res = restTemplate.postForObject(getUriForZoneOffer(geoZone),
                new CliParkingOffer(name,description,points,geoZone,type, minutes), CliParkingOffer.class);
        cliContextCollectiveOffer.getOffer().put(res.getName(), res);
        return res;
    }

    private String getUriForZoneOffer(String name) {
        return BASE_URI +"/zone/"+ contextZone.getZoneGeographic().get(name).getId() + "/";
    }

    @ShellMethod("List all gifts")
    public String listGifts() {
        return cliContextGift.getGift().toString();
    }

    @ShellMethod("List all collective offers")
    public String listCollectiveOffers() {
        return cliContextCollectiveOffer.getOffer().toString();
    }
}
