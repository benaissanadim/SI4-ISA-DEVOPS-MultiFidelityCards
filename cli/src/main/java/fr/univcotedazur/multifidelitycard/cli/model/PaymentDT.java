package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonSerialize(using = PaymentJson.class)
@NoArgsConstructor
@Getter
public class PaymentDT {
    private String creditCard;
    private String amount;

    public PaymentDT(String creditCard , String amount){
        this.amount = amount;
        this.creditCard = creditCard;
    }
}
