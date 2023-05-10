package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class CliCollectiveOffer extends CliOffer{
    protected String geographicZone;

    public CliCollectiveOffer(String name,String description, int points, String geoZone ,String type) {
        super(name,description, points, type);
        this.geographicZone = geoZone;
    }

}
