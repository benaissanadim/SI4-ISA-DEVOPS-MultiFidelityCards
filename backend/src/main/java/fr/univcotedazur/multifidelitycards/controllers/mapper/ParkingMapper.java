package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.ParkingOfferDTO;
import fr.univcotedazur.multifidelitycards.entities.ParkingOffer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParkingMapper {

    public ParkingOfferDTO toDto(ParkingOffer gift) {
        return new ParkingOfferDTO(gift.getId(),gift.getName(), gift.getDescription(),
                gift.getPoints(),gift.getGeographicZone().getName(),gift.getType(),  gift.getMinutes());
    }
    public List<ParkingOfferDTO> toDtos(List<ParkingOffer> gift) {
        return gift.stream().map(this::toDto).collect(Collectors.toList());
    }
}
