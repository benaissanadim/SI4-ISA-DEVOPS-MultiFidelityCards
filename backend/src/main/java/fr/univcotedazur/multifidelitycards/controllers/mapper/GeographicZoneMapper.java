package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.GeographicZoneDTO;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeographicZoneMapper {

    public GeographicZoneDTO toDto(GeographicZone geographicZone) {
        return new GeographicZoneDTO(geographicZone.getId(), geographicZone.getName());
    }

    public List<GeographicZoneDTO> toDtos(List<GeographicZone> geographicZones){
        return geographicZones.stream().map(this::toDto).collect(Collectors.toList());
    }

}
