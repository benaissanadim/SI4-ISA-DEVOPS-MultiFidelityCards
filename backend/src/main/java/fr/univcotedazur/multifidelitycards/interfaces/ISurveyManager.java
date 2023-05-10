package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Question;
import fr.univcotedazur.multifidelitycards.entities.Survey;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;

import java.util.List;

/**
 * survey manager
 */
public interface ISurveyManager {
    Survey createSurvey(String name, List<Question> questions);
    Question respondToQuestion(Long surveyId, Long questionId, String answer, Long clientId) throws NoSurveyFoundException;
    void deleteSurvey(Long surveyId) throws NoSurveyFoundException;
}
