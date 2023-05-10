package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FidelityCardDTO {

    private Long id;
    private String number;
    private double amount;
    private int fidelityPoints;
    private String geographicZone;
    private String owner;

}
