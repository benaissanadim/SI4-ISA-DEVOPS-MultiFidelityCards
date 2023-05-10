package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.QuestionDTO;
import fr.univcotedazur.multifidelitycards.entities.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper {

    @Autowired
    private AnswerMapper answerMapper;

    public QuestionDTO toDto(Question question) {
        return new QuestionDTO(question.getId(),answerMapper.toDtos(question.getAnswers()),question.getText());
    }

    public List<QuestionDTO> toDtos(List<Question> questions){
        return questions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Question> toList(List<QuestionDTO> questions){
        List<Question> list = new ArrayList<>();
        for(QuestionDTO q : questions){
            list.add(new Question(q.getText()));
        }
        return list;
    }
}
