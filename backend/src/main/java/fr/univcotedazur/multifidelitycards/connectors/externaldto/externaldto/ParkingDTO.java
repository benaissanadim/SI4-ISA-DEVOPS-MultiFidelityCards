package fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto;

import lombok.Getter;

@Getter
public class ParkingDTO {

    private String name;
    private int minutes;
    private String to;

    public ParkingDTO(String name, int minutes, String to) {
        this.name = name;
        this.minutes = minutes;
        this.to = to;
    }

    public ParkingDTO() {

    }
}
