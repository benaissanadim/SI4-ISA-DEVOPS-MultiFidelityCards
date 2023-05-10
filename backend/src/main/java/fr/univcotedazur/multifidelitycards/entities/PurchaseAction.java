package fr.univcotedazur.multifidelitycards.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * PurchaseAction class for purchasing with money
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@DiscriminatorValue(PurchaseAction.TRANSACTION_TYPE)
public class PurchaseAction extends Transaction {

    public static final String TRANSACTION_TYPE = "PURCHASE";

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private double amount;

    public PurchaseAction(FidelityCard fidelityCard, double amount, Store store) {
        super(fidelityCard);
        this.amount = amount;
        this.store = store;
    }
}