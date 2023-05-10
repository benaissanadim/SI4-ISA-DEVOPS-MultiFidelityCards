package fr.univcotedazur.multifidelitycards.controllers.dto;

import fr.univcotedazur.multifidelitycards.entities.OfferType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GiftDTO extends OfferDTO {

    private String store;

    public GiftDTO(Long id, String name, @NotBlank(message = "description should not be blank") String description, int points, String store ,OfferType type) {
        super(id, name, description, points, type);
        this.store = store;
    }
}
