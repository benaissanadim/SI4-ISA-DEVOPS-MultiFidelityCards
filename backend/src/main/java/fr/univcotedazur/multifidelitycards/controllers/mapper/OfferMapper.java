package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.OfferDTO;
import fr.univcotedazur.multifidelitycards.entities.Gift;
import fr.univcotedazur.multifidelitycards.entities.Offer;
import fr.univcotedazur.multifidelitycards.entities.ParkingOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferMapper {

    @Autowired
    private GiftMapper giftMapper;
    @Autowired
    private ParkingMapper collectiveOfferMapper;

    public OfferDTO toDto(Offer offer) {
        if (offer instanceof Gift) {
            return giftMapper.toDto((Gift)offer);
        }
        return collectiveOfferMapper.toDto((ParkingOffer)offer);
    }

    public List<OfferDTO> toDtos(List<Offer> offers) {
        return offers.stream().map(this::toDto).toList();
    }
}

