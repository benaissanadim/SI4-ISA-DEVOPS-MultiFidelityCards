package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Survey;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;

import java.util.List;

/**
 * survey explorator
 */
public interface ISurveyExplorator {
    List<Survey> viewSurveys() throws NoSurveyFoundException;
    Survey viewSurvey(Long surveyId) throws NoSurveyFoundException;
}
