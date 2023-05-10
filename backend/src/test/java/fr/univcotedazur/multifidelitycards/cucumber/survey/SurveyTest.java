package fr.univcotedazur.multifidelitycards.cucumber.survey;

import fr.univcotedazur.multifidelitycards.components.SurveyService;
import fr.univcotedazur.multifidelitycards.entities.Answer;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.Question;
import fr.univcotedazur.multifidelitycards.entities.Survey;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.QuestionRepository;
import fr.univcotedazur.multifidelitycards.repositories.SurveyRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SurveyTest {


    @Autowired
    private ClientRepository repository;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private String msg;
    private Client client;
    private Survey s1;
    private Question q1 ;



    @Given("client {string} and e-mail {string}")
    public void clientAndEMail(String arg0, String arg1) {
       client = repository.save(new Client(arg0, arg1));
    }


    @And("survey {string} with {int} question {string} {string}")
    public void surveyWithQuestion(String arg0, int arg1, String arg2, String arg3) {
        surveyRepository.deleteAll();
        s1 = surveyService.createSurvey(arg0, List.of(new Question(arg2), new Question(arg3)));
    }

    @When("admin adds survey {string} with questions {string} , {string}")
    public void adminAddsSurveyWithQuestions(String arg0, String arg1, String arg2) {
        surveyService.createSurvey(arg0, List.of(new Question(arg1), new Question(arg2)));
    }


    @Then("survey {string} added successfully")
    public void surveyAddedSuccessfully(String arg0) {
        List<Survey> surveys = surveyRepository.findAll();
        Assertions.assertEquals(arg0, surveys.get(0).getName());
    }

    @And("number of survies equal to {int}")
    public void numberOfSurviesEqualTo(int arg0) {
        Assertions.assertEquals(arg0, surveyRepository.count());
    }

    @When("client respond to question {int} {string} of survey {string}")
    public void clientRespondToQuestionOfSurvey(int arg0, String arg1, String arg2) throws NoSurveyFoundException {
        q1 = surveyService.respondToQuestion(s1.getId(),1L , arg1, client.getId());
    }

    @Then("question {int} for survey has {int} answer {string} from {string}")
    public void questionForSurveyHasAnswerFrom(int arg0, int arg1, String arg2, String arg3) {
        Assertions.assertEquals(arg1, q1.getAnswers().size());
        Answer a = q1.getAnswers().get(0);
        Assertions.assertEquals(arg2, a.getText());
        Assertions.assertEquals(arg3, a.getClient().getUsername());


    }

    @When("admin delete survey {string}")
    public void adminDeleteSurvey(String arg0) throws NoSurveyFoundException {
        surveyService.deleteSurvey(s1.getId());
    }

    @And("number of questions equal to {int}")
    public void numberOfQuestionsEqualTo(int arg0) {
        Assertions.assertEquals(arg0, questionRepository.count());
    }

}