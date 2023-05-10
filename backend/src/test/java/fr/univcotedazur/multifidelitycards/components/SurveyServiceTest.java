package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyExplorator;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyManager;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.QuestionRepository;
import fr.univcotedazur.multifidelitycards.repositories.SurveyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional

public class SurveyServiceTest {
    private Survey survey;
    private Question question1;
    private Question question2;
    private List<Question> questions;
    @Autowired
    private ISurveyExplorator surveyExplorator;
    @Autowired
    private ISurveyManager surveyManager;
    @Autowired
    private SurveyRepository SurveyRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        question1 = new Question("how did you like the service?");
        question2 = new Question("is the parking service efficient?");
        questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
    }
    @Test
    public void createSurveyTest() {
        survey = surveyManager.createSurvey("survey1", questions);
        Assertions.assertEquals(1, this.SurveyRepository.count());
        Assertions.assertEquals(survey, this.SurveyRepository.findById(survey.getId()).get());
    }
    @Test
    public void ViewSurveyTest() throws NoSurveyFoundException {
        survey = surveyManager.createSurvey("survey2", questions);
        List<Survey> surveys = surveyExplorator.viewSurveys();
        Assertions.assertEquals(1, this.SurveyRepository.count());
        Assertions.assertEquals(survey, this.SurveyRepository.findById(survey.getId()).get());
        Assertions.assertEquals(surveys, this.SurveyRepository.findAll());
        

    }
    @Test
    public void ViewSurveyByIdTest() throws NoSurveyFoundException {
        survey = surveyManager.createSurvey("survey2", questions);
        Survey survey1 = surveyExplorator.viewSurvey(survey.getId());
        Assertions.assertEquals(1, this.SurveyRepository.count());
        Assertions.assertEquals(survey, this.SurveyRepository.findById(survey.getId()).get());
        Assertions.assertEquals(survey1, this.SurveyRepository.findById(survey.getId()).get());
    }

    @Test
    public void respondToSurvey() throws NoSurveyFoundException {

        Long clientId = 1L;
        String answer = "Yes";

        Survey survey = surveyManager.createSurvey("survey2",questions);
        Client client = new Client("pjr","opzjfppz@gmail.com");
        clientRepository.save(client);

        surveyManager.respondToQuestion(survey.getId(), 1L, answer, client.getId());
        assertEquals(question1.getAnswers().size(), 1);
        assertEquals(question1.getAnswers().get(0).getClient().getId(), client.getId());
        assertEquals(question1.getAnswers().get(0).getText(), answer);
    }
    @Test
    public void deleteSurveyTest() throws NoSurveyFoundException {
        survey = surveyManager.createSurvey("survey2", questions);
        surveyManager.deleteSurvey(survey.getId());
        Assertions.assertEquals(0, this.SurveyRepository.count());
        Assertions.assertEquals(0, this.questionRepository.count());
        


    }
    @Test
    public void deleteSurveyTestNoSurveyFound()  {
        assertThrows(NoSurveyFoundException.class, () -> {
            surveyManager.deleteSurvey(2L);
        });
    }



}
