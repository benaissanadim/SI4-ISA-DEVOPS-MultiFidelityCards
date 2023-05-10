package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliFidelityCard;
import fr.univcotedazur.multifidelitycard.cli.model.CliZoneGeographic;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContextFidelityCard {
    private Map<String, CliFidelityCard> fidelityCardMap;


    public Map<String, CliFidelityCard> getFidelityCard() {
        return fidelityCardMap;
    }

    public CliContextFidelityCard() {
        fidelityCardMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String s = "******************** LISTING ALL FIDELITY-CARDS ********************";
        sb.append(s);
        for (Map.Entry<String, CliFidelityCard> entry : fidelityCardMap.entrySet()) {
            sb.append("\n");
            sb.append("\t").append(entry.getValue());
        }
        return sb.toString();
    }
}
