package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

/**
 * Store class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(of = {"name"})
@Table(name = "store")
@EqualsAndHashCode(of =  {"name"})
public class Store {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String address;


    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Schedule> schedule;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private GeographicZone zone;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Gift> gifts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "store_client_favorite",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Set<Client> clientsFavorite = new HashSet<>();

    public Store(String name, String address, List<Schedule> schedule , GeographicZone zone){
        this.name = name;
        this.zone = zone;
        this.address = address;
        this.schedule = schedule;
        this.gifts = new ArrayList<>();
    }

    public void addSchedule(Schedule schedule) {
        this.schedule.add(schedule);
        schedule.setStore(this);
    }

    public void addToFavourite(Client client) {
        this.clientsFavorite.add(client);
    }
}