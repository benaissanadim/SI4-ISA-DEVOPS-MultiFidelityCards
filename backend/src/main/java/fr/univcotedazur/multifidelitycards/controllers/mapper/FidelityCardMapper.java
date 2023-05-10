package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.FidelityCardDTO;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FidelityCardMapper {

    public FidelityCardDTO toDto(FidelityCard fidelityCard) {
        return new FidelityCardDTO(fidelityCard.getId(), fidelityCard.getNumber(), fidelityCard.getAmount(),
                fidelityCard.getPoints(), fidelityCard.getGeographicZone().getName(), fidelityCard.getClient().getUsername());
    }

    public List<FidelityCardDTO> toDtos(List<FidelityCard> fidelityCards){
        return fidelityCards.stream().map(this::toDto).collect(Collectors.toList());
    }

}
