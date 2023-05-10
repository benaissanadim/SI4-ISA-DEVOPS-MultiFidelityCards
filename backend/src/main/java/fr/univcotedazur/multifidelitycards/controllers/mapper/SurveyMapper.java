package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.SurveyDTO;
import fr.univcotedazur.multifidelitycards.entities.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SurveyMapper {

    @Autowired
    private QuestionMapper questionMapper;

    public SurveyDTO toDto(Survey survey) {
        return new SurveyDTO(survey.getId(), survey.getName(), questionMapper.toDtos(survey.getQuestions()));
    }
}
