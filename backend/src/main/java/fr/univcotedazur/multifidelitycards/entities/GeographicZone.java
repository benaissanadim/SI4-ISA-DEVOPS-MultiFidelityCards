package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;

/**
 * GeographicZone class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "geographic_zones")
@EqualsAndHashCode(of = "name")
public class GeographicZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String name;

    public GeographicZone(String name) {
        this.name = name;
    }
}
