package fr.univcotedazur.multifidelitycards.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * KPIs of offers
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KPIsOffers {
    private YearMonth monthYear;
    private int nbGifts ;
    private double giftsEvolution;
}
