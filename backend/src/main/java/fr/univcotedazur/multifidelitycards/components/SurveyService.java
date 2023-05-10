package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Answer;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.Question;
import fr.univcotedazur.multifidelitycards.entities.Survey;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyExplorator;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyManager;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * SurveyService for creating and responding survies
 */
@Transactional
@Component
public class SurveyService implements ISurveyExplorator, ISurveyManager {


    private final SurveyRepository surveyRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository , ClientRepository clientRepository){
    this.surveyRepository = surveyRepository;
        this.clientRepository = clientRepository;
    }
    @Override
    public Survey createSurvey(String name, List<Question> questions) {
        if (questions == null) {
            throw new IllegalArgumentException("Questions cannot be null");
        }
        Survey survey = new Survey(name,questions);
        surveyRepository.save(survey);
        return survey;
    }

    @Override
    public List<Survey> viewSurveys() throws NoSurveyFoundException {
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            throw new NoSurveyFoundException("No surveys found in repository");
        }
        return surveys;
    }

    @Override
    public Survey viewSurvey(Long surveyId) throws NoSurveyFoundException {
        Optional<Survey> survey = surveyRepository.findById(surveyId);
        if (survey.isEmpty()) {
            throw new NoSurveyFoundException("Survey not found with ID: " + surveyId);
        }
        return survey.get();
    }

    @Override
    public Question respondToQuestion(Long surveyId, Long questionId, String answer, Long clientId) throws NoSurveyFoundException {
        Client client = clientRepository.findById(clientId).orElseThrow(() ->
                new IllegalArgumentException("Client not found with ID: " + clientId));
        Survey survey = viewSurvey(surveyId) ;

        Question question = survey.getQuestions().get(questionId.intValue()-1);
        Answer response = new Answer(question,answer, client);
        System.out.println(response);
        question.getAnswers().add(response);
        surveyRepository.save(survey);

            return question;
    }

    @Override
    public void deleteSurvey(Long surveyId) throws NoSurveyFoundException {
        try {
            Survey survey = viewSurvey(surveyId);
            surveyRepository.delete(survey);
        }
        catch (NoSurveyFoundException e) {
            throw new NoSurveyFoundException("Survey not found with ID: " + surveyId);
        }

    }
}
