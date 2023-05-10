package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KPIsCA {
    private YearMonth monthYear;
    private double ca;
    private double caEvolution;

    @Override public String toString() {
        return "KPIsCA {" + "monthYear=" + monthYear + ", ca=" + ca + ", caEvolution=" + caEvolution + "%}";
    }
}
