package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ShellComponent
public class ClientCommands {

    public static final String BASE_URI = "/clients";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CliClientContext cliContext;

    @ShellMethod("Register a client in the CoD backend (register CUSTOMER_NAME CUSTOMER_EMAIL)")
    public CliClient register(String name, String email) {
        try{
            CliClient res = restTemplate.postForObject(BASE_URI, new CliClient(name, email), CliClient.class);
            cliContext.getCustomers().put(res.getUsername(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @ShellMethod("List all customers")
    public String listClients() {
        ResponseEntity<List<CliClient>> responseEntityClients = restTemplate.exchange(
                ClientCommands.BASE_URI, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CliClient>>() {
                });
        for(CliClient cliClient : responseEntityClients.getBody()) {
            cliContext.getCustomers().put(cliClient.getUsername(), cliClient);
        }
        return cliContext.toString();
    }

}
