package fr.univcotedazur.multifidelitycards.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * KPIs of chiffre affaires
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KPIsCA {
    private YearMonth monthYear;
    private double ca;
    private double caEvolution;
}
