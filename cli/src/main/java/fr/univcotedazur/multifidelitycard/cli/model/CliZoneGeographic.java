package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CliZoneGeographic {

    private String name;
    private Long id;
    public CliZoneGeographic(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GeographicZone : {name=" + name + "}";
    }

}
