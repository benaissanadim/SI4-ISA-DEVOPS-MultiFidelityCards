package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDTO {

    private String name;
    private int minutes;
    private String to;
}
