package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliOffer;
import fr.univcotedazur.multifidelitycard.cli.model.CliZoneGeographic;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContextOffer {
    private Map<String, CliOffer> offerMap;


    public Map<String, CliOffer> getOffer() {
        return offerMap;
    }

    public CliContextOffer() {
        offerMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return offerMap.keySet().stream()
                .map(key -> key + "=" + offerMap.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
