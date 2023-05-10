package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextStore;
import fr.univcotedazur.multifidelitycard.cli.model.CliSchedule;
import fr.univcotedazur.multifidelitycard.cli.model.CliStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ShellComponent
public class StoreCommands {
    //

    public static final String BASE_URI = "/stores";

    @Autowired RestTemplate restTemplate;

    @Autowired CliContextStore cliContextStore;
    @Autowired CliClientContext cliContextClient;

    @ShellMethod("Register a store in the CoD backend (STORE_NAME STORE_ADDRESS ZONE_NAME)")
    public CliStore registerStore(String name, String address, String zone) {
        try {
            CliStore res = restTemplate.postForObject(BASE_URI, new CliStore(null,name, address,null, zone),
                    CliStore.class);
            cliContextStore.getStoreMap().put(res.getName(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @ShellMethod("Register a store in the CoD backend (STORE_NAME BEGIN_TIME END_TIME DAY_WEEK)")
    public CliStore editStoreSchedule(String store, String beginTime, String endTime, String dayOfWeek) {
        CliSchedule schedule =  new CliSchedule(null,beginTime, endTime,dayOfWeek);
        restTemplate.put(getUriForStore(store)+"/schedule",schedule, CliStore.class);

        ResponseEntity<List<CliStore>> responseEntityStores = restTemplate.exchange(
                StoreCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        for(CliStore cliStore : responseEntityStores.getBody()) {
            cliContextStore.getStoreMap().put(cliStore.getName(), cliStore);
            if(cliStore.getName().equals(store)){
                return cliStore;
            }
        }
        return null;
    }

    @ShellMethod("Add store to favourite (STORE_NAME CLIENT_NAME) ")
    public String addToFavourite(String storeName, String clientName){
        return restTemplate.postForObject(getUriForStore(storeName)+"/favourites/client/"
                +getIdClientForName(clientName),
                new CliStore(), String.class);
    }

    @ShellMethod("Get Store favourite clients (STORE_NAME) ")
    public String getFavouriteClients(String storeName){
        return restTemplate.getForObject(getUriForStore(storeName)+"/favourites", String.class);
    }

    private String getUriForStore(String name) {
        return BASE_URI +"/"+ cliContextStore.getStoreMap().get(name).getId();
    }

    private Long getIdClientForName(String name){
        return cliContextClient.getCustomers().get(name).getId();
    }

    @ShellMethod("List all stores")
    public String listStores() {
        return cliContextStore.toString();
    }
}
