package fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * External DTO (Data Transfert Object) to POST payment to the external Bank system
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String creditCard;
    private double amount;
}
