package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * Client class
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"username"})
@Entity
@Table(name = "client")
@ToString()
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FidelityCard fidelityCard;

    public Client(String username, String email) {
        this.username = username;
        this.email = email;
        this.status = ClientStatus.NORMAL;
    }


}
