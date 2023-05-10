package fr.univcotedazur.multifidelitycards.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * OfferUseAction for purchasing offers
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue(OfferUseAction.TRANSACTION_TYPE)
public class OfferUseAction extends Transaction {

    public static final String TRANSACTION_TYPE = "OFFER_USE";

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    public OfferUseAction(FidelityCard fidelityCard, Offer relatedOffer) {
        super(fidelityCard);
        this.offer = relatedOffer;
    }

}
