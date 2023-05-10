package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import fr.univcotedazur.multifidelitycard.cli.model.CliZoneGeographic;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContextZone {

    private Map<String, CliZoneGeographic> zoneGeographicMap;


    public Map<String, CliZoneGeographic> getZoneGeographic() {
        return zoneGeographicMap;
    }

    public CliContextZone() {
        zoneGeographicMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String s = "******************** LISTING ALL GEOGRAPHIC-ZONES ********************";
        sb.append(s);
        for (Map.Entry<String, CliZoneGeographic> entry : zoneGeographicMap.entrySet()) {
            sb.append("\n");
            sb.append("\t").append(entry.getValue());
        }
        return sb.toString();
    }
}
