package fr.univcotedazur.multifidelitycards.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Offer class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@ToString(of = {"name", "points", "description", "type"})
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    private int points;
    private String description;
    private LocalDate createdOn;

    @Enumerated(EnumType.STRING)
    private OfferType type;


    public Offer(String name, String description, int points, OfferType type) {
        this.name = name;
        this.points = points;
        this.description = description;
        this.type = type;
        this.createdOn = LocalDate.now();

    }

}

