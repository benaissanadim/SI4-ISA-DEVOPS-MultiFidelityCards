package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliGift;
import fr.univcotedazur.multifidelitycard.cli.model.CliOffer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class CliContextGift {

    private Map<String, CliGift> giftMap;


    public Map<String, CliGift> getGift() {
        return giftMap;
    }

    public CliContextGift() {
        giftMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return giftMap.keySet().stream()
                .map(key -> key + "=" + giftMap.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
