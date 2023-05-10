package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {

    private long id;
    private List<AnswerDTO> answers;
    @NotBlank(message = "text should not be blank")
    private String text;
}
