package fr.univcotedazur.multifidelitycard.cli.context;

import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import fr.univcotedazur.multifidelitycard.cli.model.CliStore;
import fr.univcotedazur.multifidelitycard.cli.model.CliSurvey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContextSurvey {

    private Map<String, CliSurvey> surveyMap;


    public Map<String, CliSurvey> getSurveyMap() {
        return surveyMap;
    }

    public CliContextSurvey() {
        surveyMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String s = "******************** LISTING ALL SURVIES ********************";
        sb.append(s);
        for (Map.Entry<String, CliSurvey> entry : surveyMap.entrySet()) {
            sb.append("\n");
            sb.append("\t").append(entry.getValue());
        }
        return sb.toString();
    }
}
