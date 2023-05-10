package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CliContextStore {
    private Map<String, CliStore> storeMap;


    public Map<String, CliStore> getStoreMap() {
        return storeMap;
    }

    public CliContextStore() {
        storeMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String s = "******************** LISTING ALL STORES ********************";
        sb.append(s);
        for (Map.Entry<String, CliStore> entry : storeMap.entrySet()) {
            sb.append("\n");
            sb.append("\t").append(entry.getValue());
        }
        return sb.toString();
    }
}
