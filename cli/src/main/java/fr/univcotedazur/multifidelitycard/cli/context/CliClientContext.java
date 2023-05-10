package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CliClientContext {

    private Map<String, CliClient> customers;

    public Map<String, CliClient> getCustomers() {
        return customers;
    }

    public CliClientContext() {
        customers = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String s = "******************** LISTING ALL CLIENTS ********************";
        sb.append(s);
        for (Map.Entry<String, CliClient> entry : customers.entrySet()) {
            sb.append("\n");
            sb.append("\t").append(entry.getValue());
        }
        return sb.toString();
    }

}
