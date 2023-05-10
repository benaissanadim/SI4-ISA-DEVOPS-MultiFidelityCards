package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CliFidelityCard {

    private String number;
    private double amount;
    private int fidelityPoints;
    private String owner;
    private String geographicZone;
    private Long id;

    public CliFidelityCard(String clientUsername, String zoneGeographic) {
        this.owner = clientUsername;
        this.geographicZone = zoneGeographic;
    }

    @Override
    public String toString() {
        return "FidelityCard : {number=" + number + ", amount=" + amount + ", fidelityPoints=" + fidelityPoints + ", owner=" + owner + ", geographicZone=" + geographicZone + "}";
    }
}
