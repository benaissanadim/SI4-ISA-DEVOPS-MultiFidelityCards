package fr.univcotedazur.multifidelitycard.cli.commands;

import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextSurvey;
import fr.univcotedazur.multifidelitycard.cli.model.CliAnswer;
import fr.univcotedazur.multifidelitycard.cli.model.CliQuestion;
import fr.univcotedazur.multifidelitycard.cli.model.CliSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class SurveyCommands {

    public static final String BASE_URI = "/surveys";


    @Autowired RestTemplate restTemplate;
    @Autowired CliContextSurvey cliContextSurvey;
    @Autowired CliClientContext cliContext;

    @ShellMethod("Register a survey in the CoD backend (register SURVEY_NAME, QUESTIONS)")
    public CliSurvey addSurvey(String name, String questions) {
        String[] questionArray =  questions.substring(1, questions.length() - 1).
                split("\\]\\s*\\[");

        List<CliQuestion> list = new ArrayList<>();
        for(String q : questionArray) {
            list.add(new CliQuestion(q));
        }
        CliSurvey survey = new CliSurvey(name, list);

        CliSurvey res = restTemplate.postForObject(BASE_URI, survey, CliSurvey.class);
        cliContextSurvey.getSurveyMap().put(res.getName(), res);
        return res;
    }


    @ShellMethod("reply to survey question")
    public CliQuestion reply(String surveyName, int questionNumber, String answer, String clientName) {
        return restTemplate.postForObject(BASE_URI + "/" + cliContextSurvey.getSurveyMap().get(surveyName).getId() + "/question/"+
                questionNumber + "/reply/" + cliContext.getCustomers().get(clientName).getId()
                , new CliAnswer(answer, clientName), CliQuestion.class);
    }

    @ShellMethod("delete Survey NAME")
    public String delete(String surveyName) {
        try {
            restTemplate.delete(BASE_URI + "/" + cliContextSurvey.getSurveyMap().get(surveyName).getId(), new CliSurvey(), String.class);
            cliContextSurvey.getSurveyMap().remove(surveyName);
            return "Survey deleted";
        }catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod("list-all-Surveys")
    public String listSurveys() {
        return cliContextSurvey.toString();
    }

}
