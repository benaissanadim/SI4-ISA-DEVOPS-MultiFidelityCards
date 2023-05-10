package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CliOffer {
    protected Long id ;
    protected String name;
    protected int points;
    protected String description;
    protected String type;

    public CliOffer(String name, String description, int points, String type) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.type = type;
    }

}
