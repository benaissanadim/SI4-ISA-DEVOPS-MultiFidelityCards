package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.AnswerDTO;
import fr.univcotedazur.multifidelitycards.entities.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerMapper {

    public AnswerDTO toDto(Answer answer) {
        return new AnswerDTO(answer.getId(), answer.getClient().getUsername(), answer.getText());
    }

    public List<AnswerDTO> toDtos(List<Answer> answers){
        return answers.stream().map(this::toDto).collect(Collectors.toList());
    }
}
