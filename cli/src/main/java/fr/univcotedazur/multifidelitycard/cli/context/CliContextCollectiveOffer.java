package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliCollectiveOffer;
import fr.univcotedazur.multifidelitycard.cli.model.CliGift;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContextCollectiveOffer {

    private Map<String, CliCollectiveOffer> collectiveOfferMap;


    public Map<String, CliCollectiveOffer> getOffer() {
        return collectiveOfferMap;
    }

    public CliContextCollectiveOffer() {
        collectiveOfferMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return collectiveOfferMap.keySet().stream()
                .map(key -> key + "=" + collectiveOfferMap.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
