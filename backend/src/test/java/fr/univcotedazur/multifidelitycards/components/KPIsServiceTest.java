package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class KPIsServiceTest {

    @MockBean
    private HistoryService storeHistory;

    @Autowired
    KPIsService kpisService;

    @Test
    public void testNbPurchasesPerDayInStore() {
        Map<YearMonth, Double> map = new HashMap<>();
        map.put(YearMonth.of(2023,Month.JANUARY), 100.0);
        map.put(YearMonth.of(2023,Month.FEBRUARY), 110.0);
        map.put(YearMonth.of(2023,Month.MARCH), 145.0);
        map.put(YearMonth.of(2023,Month.APRIL), 120.0);
        map.put(YearMonth.of(2023,Month.MAY), 130.0);

        when(storeHistory.getPurchaseHistoryPerMonth(any(Long.class))).thenReturn(map);
        List<KPIsCA> kpIsCAList = kpisService.getKPIsCAEvolution(1L);
        assertEquals(5,kpIsCAList.size());
        KPIsCA kpIsCA1 = kpIsCAList.get(0);
        KPIsCA kpIsCA2 = kpIsCAList.get(1);
        KPIsCA kpIsCA3 = kpIsCAList.get(2);
        KPIsCA kpIsCA4 = kpIsCAList.get(3);
        KPIsCA kpIsCA5 = kpIsCAList.get(4);
        assertEquals(YearMonth.of(2023,Month.JANUARY),kpIsCA1.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.FEBRUARY),kpIsCA2.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.MARCH),kpIsCA3.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.APRIL),kpIsCA4.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.MAY),kpIsCA5.getMonthYear());

        assertEquals(100.0,kpIsCA1.getCa());
        assertEquals(110.0,kpIsCA2.getCa());
        assertEquals(145.0,kpIsCA3.getCa());
        assertEquals(120.0,kpIsCA4.getCa());
        assertEquals(130.0,kpIsCA5.getCa());

        assertEquals(0,kpIsCA1.getCaEvolution());
        assertEquals(10.0,kpIsCA2.getCaEvolution());
        assertEquals(31.82,kpIsCA3.getCaEvolution());
        assertEquals(-17.24,kpIsCA4.getCaEvolution());
        assertEquals(8.33,kpIsCA5.getCaEvolution());

    }

    @Test
    public void testKpiGiftsInStore() {
        Map<YearMonth, Integer> map = new HashMap<>();
        map.put(YearMonth.of(2023,Month.JANUARY), 5);
        map.put(YearMonth.of(2023,Month.FEBRUARY), 7);
        map.put(YearMonth.of(2023,Month.MARCH), 10);
        map.put(YearMonth.of(2023,Month.APRIL), 8);
        map.put(YearMonth.of(2023,Month.MAY), 12);

        when(storeHistory.getGiftsHistoryPerMonth(any(Long.class))).thenReturn(map);
        List<KPIsOffers> kpIsCAList = kpisService.getKPIsGiftsEvolution(1L);
        assertEquals(5,kpIsCAList.size());
        KPIsOffers kpIsCA1 = kpIsCAList.get(0);
        KPIsOffers kpIsCA2 = kpIsCAList.get(1);
        KPIsOffers kpIsCA3 = kpIsCAList.get(2);
        KPIsOffers kpIsCA4 = kpIsCAList.get(3);
        KPIsOffers kpIsCA5 = kpIsCAList.get(4);
        assertEquals(YearMonth.of(2023,Month.JANUARY),kpIsCA1.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.FEBRUARY),kpIsCA2.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.MARCH),kpIsCA3.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.APRIL),kpIsCA4.getMonthYear());
        assertEquals(YearMonth.of(2023,Month.MAY),kpIsCA5.getMonthYear());

        assertEquals(5,kpIsCA1.getNbGifts());
        assertEquals(7,kpIsCA2.getNbGifts());
        assertEquals(10,kpIsCA3.getNbGifts());
        assertEquals(8,kpIsCA4.getNbGifts());
        assertEquals(12,kpIsCA5.getNbGifts());

        assertEquals(0,kpIsCA1.getGiftsEvolution());
        assertEquals(40.0,kpIsCA2.getGiftsEvolution());
        assertEquals(42.86,kpIsCA3.getGiftsEvolution());
        assertEquals(-20.00,kpIsCA4.getGiftsEvolution());
        assertEquals(50.0,kpIsCA5.getGiftsEvolution());

    }


}
