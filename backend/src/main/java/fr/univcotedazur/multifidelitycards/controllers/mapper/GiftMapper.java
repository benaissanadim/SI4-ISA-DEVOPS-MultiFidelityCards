package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.GiftDTO;
import fr.univcotedazur.multifidelitycards.entities.Gift;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GiftMapper {

    public GiftDTO toDto(Gift gift) {
        return new GiftDTO(gift.getId(),gift.getName(), gift.getDescription(),
                gift.getPoints(), gift.getStore().getName(),
                gift.getType());
    }

    public List<GiftDTO> toDtos(List<Gift> gifts) {
        List<GiftDTO> giftDTOs = new ArrayList<>();
        for (Gift gift : gifts) {
            giftDTOs.add(toDto(gift));
        }
        return giftDTOs;
    }
}
