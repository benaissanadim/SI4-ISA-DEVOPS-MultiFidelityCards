package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Transaction is the base class for all transactions
 */
@Getter
@Setter
@Entity
@ToString(of = {"fidelityCard", "transactionType"})
@NoArgsConstructor
@EqualsAndHashCode(of = {"fidelityCard", "transactionType"})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fidelity_card_id")
    private FidelityCard fidelityCard;

    @Column(name = "transaction_type", insertable = false, updatable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private LocalDateTime createdAt;

    public Transaction(FidelityCard fidelityCard) {
        this.fidelityCard = fidelityCard;
        this.createdAt = LocalDateTime.now();
    }
}
