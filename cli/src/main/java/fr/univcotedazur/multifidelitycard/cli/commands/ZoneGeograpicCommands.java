package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.CliContextZone;
import fr.univcotedazur.multifidelitycard.cli.model.CliZoneGeographic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class ZoneGeograpicCommands {

    public static final String BASE_URI = "/geographicZones";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CliContextZone cliContextZone;

    @ShellMethod("Register a zone in the CoD backend (register ZONE_NAME)")
    public CliZoneGeographic add(String name) {
        try{
            CliZoneGeographic res = restTemplate.postForObject(BASE_URI , new CliZoneGeographic(name), CliZoneGeographic.class);
            cliContextZone.getZoneGeographic().put(res.getName(), res);
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @ShellMethod("List all zones geographic")
    public String listZones() {
        return cliContextZone.toString();
    }

}
