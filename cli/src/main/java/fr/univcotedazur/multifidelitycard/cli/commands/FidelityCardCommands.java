package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.*;
import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class FidelityCardCommands {

    public static String BASE_URI ="/fidelityCards";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CliContextFidelityCard cliContext;

    @Autowired
    private CliContextCollectiveOffer cliContextCollectiveOffer;

    @Autowired
    private CliClientContext contextClient;

    @Autowired
    private CliContextZone contextZone;

    @Autowired
    private CliContextGift cliContextGift;


    @ShellMethod("Register a fidelity card in the CoD backend (CLIENT_NAME ZONE_NAME) ")
    public CliFidelityCard addFidelityCard(String clientName, String geographicZone) {
        CliFidelityCard card = new CliFidelityCard();
        card.setGeographicZone(geographicZone);
        CliFidelityCard res = restTemplate.postForObject(getUriCreateCard(clientName),
                card, CliFidelityCard.class);
        cliContext.getFidelityCard().put(res.getNumber(), res);
        return res;
    }

    @ShellMethod("List all fidelity cards")
    public String listFidelityCards() {
        return cliContext.toString();
    }

    @ShellMethod("Recharge fidelity card (Fidelity_CARD_NUMBER AMOUNT CREDIT_CARD_NUMBER")
    public CliFidelityCard rechargeFidelityCard(String cardNumber, String amount, String creditCard) {
        try{
            CliFidelityCard res = restTemplate.postForObject(getUriForCard(cardNumber)+"/recharge",
                    new PaymentDT(creditCard, amount), CliFidelityCard.class);
            cliContext.getFidelityCard().put(res.getNumber(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @ShellMethod("Pay with fidelity card (Fidelity_CARD_NUMBER AMOUNT STORE_NAME")
    public CliFidelityCard payWithFidelityCard(String cardNumber, double amount, String store) {
        try{
            CliFidelityCard res = restTemplate.postForObject(getUriForCard(cardNumber)+"/pay",
                    new PurchaseDTO(store,amount), CliFidelityCard.class);
            cliContext.getFidelityCard().put(res.getNumber(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @ShellMethod("RECEIVE GIFT(Fidelity_CARD_NUMBER GIFT_NAME")
    public CliFidelityCard ReceiveGift(String cardNumber, String gift) {
        try{
            CliFidelityCard res = restTemplate.postForObject(getUriForCard(cardNumber)+"/gift/"+getGiftId(gift),
                    new CliFidelityCard(), CliFidelityCard.class);
            cliContext.getFidelityCard().put(res.getNumber(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @ShellMethod("RECEIVE GIFT(Fidelity_CARD_NUMBER PARKING_NAME")
    public CliFidelityCard useParking(String cardNumber, String parking) {
        try{
            CliFidelityCard res = restTemplate.postForObject(getUriForCard(cardNumber)+"/parking/"+
                            getParkingId(parking), new CliFidelityCard(), CliFidelityCard.class);
            cliContext.getFidelityCard().put(res.getNumber(), res);
            return res;
        }catch (Exception e){
            return null;
        }
    }

    private Long getGiftId(String name) {
        return cliContextGift.getGift().get(name).getId() ;
    }

    private Long getParkingId(String name) {
        return cliContextCollectiveOffer.getOffer().get(name).getId() ;
    }

    private String getUriForCard(String number) {
        return "/fidelityCards/"+ cliContext.getFidelityCard().get(number).getId() ;
    }

    private Long getIdForZone(String zone){
        return contextZone.getZoneGeographic().get(zone).getId();
    }

    private String getUriPayCard(String clientName){
        return getUriForClient(clientName)+"/pay";
    }


    private String getUriCreateCard(String clientName){
        return getUriForClient(clientName);
    }

    private String getUriForClient(String name) {
        return "/clients/" + contextClient.getCustomers().get(name).getId() + "/fidelityCard";
    }

}
