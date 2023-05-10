package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseDTO {

    private String store;
    @Positive
    private double amount;
}
