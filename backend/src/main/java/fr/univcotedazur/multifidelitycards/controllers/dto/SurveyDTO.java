package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SurveyDTO {
    private Long id;
    private String name;
    private List<QuestionDTO> questions;

}
