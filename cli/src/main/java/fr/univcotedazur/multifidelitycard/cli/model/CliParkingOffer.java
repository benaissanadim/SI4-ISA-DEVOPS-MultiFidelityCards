package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CliParkingOffer extends CliCollectiveOffer{
    private int minutes;

    public CliParkingOffer(String name, String description, int points, String geoZone, String type, int minutes) {
        super(name, description, points, geoZone, type);
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "CliParkingOffer{" + "minutes=" + minutes + ", name=" + name + ", description=" + description + ", points=" + points + ", geographicZone=" + geographicZone + ", type=" + type + '}';
    }
}
