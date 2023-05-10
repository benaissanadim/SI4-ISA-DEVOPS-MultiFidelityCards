package fr.univcotedazur.multifidelitycards.controllers.dto;

import fr.univcotedazur.multifidelitycards.entities.OfferType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParkingOfferDTO extends CollectiveOfferDTO {

    private int minutes;

    public ParkingOfferDTO(Long id, @NotBlank String name, @NotBlank String description, int points, String geographicZone, OfferType type, @Positive
            int minutes) {
        super(id, name, description, points, geographicZone, type);
        this.minutes = minutes;
    }


}
