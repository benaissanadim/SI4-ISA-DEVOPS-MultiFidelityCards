package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnswerDTO {

    private long id;

    private String clientName;
    @NotBlank(message = "text should not be blank")
    private String text;

    public AnswerDTO(Long id,String clientName, String text) {
        this.id = id;
        this.clientName = clientName;
        this.text = text;
    }
}
