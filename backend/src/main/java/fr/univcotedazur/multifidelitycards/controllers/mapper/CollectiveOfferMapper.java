package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.CollectiveOfferDTO;
import fr.univcotedazur.multifidelitycards.entities.CollectiveOffer;
import org.springframework.stereotype.Component;


@Component
public class CollectiveOfferMapper {

    public CollectiveOfferDTO toDto(CollectiveOffer collectiveOffer) {
        return new CollectiveOfferDTO(collectiveOffer.getId(), collectiveOffer.getName(), collectiveOffer.getDescription(),
                collectiveOffer.getPoints(), collectiveOffer.getGeographicZone().getName(),
                collectiveOffer.getType());
    }
}
