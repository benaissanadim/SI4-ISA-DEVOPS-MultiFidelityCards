package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * FidelityCard class
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"number"})
@Table(name = "fidelity_card")
@ToString(of={"number", "amount", "points"})
public class FidelityCard {

    @Id
    @GeneratedValue
    private Long id ;
    private String number;
    private int points;
    private double amount;

    @ManyToOne
    private GeographicZone geographicZone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;


    @OneToMany(mappedBy = "fidelityCard", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public FidelityCard(Client client, GeographicZone geographicZone) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        this.number = sb.toString();
        this.client = client;
        this.amount = 0;
        this.points = 0;
        this.transactions = new ArrayList<>();
        this.geographicZone = geographicZone;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setFidelityCard(this);
    }
}
