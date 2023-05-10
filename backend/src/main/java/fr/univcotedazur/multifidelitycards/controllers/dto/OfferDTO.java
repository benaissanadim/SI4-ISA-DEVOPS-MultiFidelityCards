package fr.univcotedazur.multifidelitycards.controllers.dto;

import fr.univcotedazur.multifidelitycards.entities.OfferType;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OfferDTO {

    private Long id;

    private String name;
    @NotBlank(message = "description should not be blank")
    private String description;
    private int points;
    private OfferType type;

}
