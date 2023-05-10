package fr.univcotedazur.multifidelitycards.controllers.dto;

import fr.univcotedazur.multifidelitycards.entities.OfferType;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CollectiveOfferDTO extends OfferDTO {
    private String geographicZone;


    public CollectiveOfferDTO(Long id, String name,  @NotBlank(message = "description should not be blank") String description,int points, String geographicZone,OfferType type) {
        super(id, name,description, points, type);
        this.geographicZone = geographicZone;
    }
}
