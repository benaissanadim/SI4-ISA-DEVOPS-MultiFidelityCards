package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.YearMonth;

@AllArgsConstructor
@ToString
@Getter
public class KPIsOffers {
    private YearMonth monthYear;
    private int nbGifts ;
    private double giftsEvolution;

    public String toString() {
        return "KPIsOffers {" + "monthYear=" + monthYear + ", nbGifts=" + nbGifts + ", giftsEvolution=" + giftsEvolution + "%}";
    }
}
