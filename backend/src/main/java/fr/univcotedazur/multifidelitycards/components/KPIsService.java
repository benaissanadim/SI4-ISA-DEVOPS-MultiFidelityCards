package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.KPIsCA;
import fr.univcotedazur.multifidelitycards.entities.KPIsOffers;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreKPIs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * KPIs Service responsable for creating KPIs
 */
@Transactional
@Component
public class KPIsService implements IStoreKPIs {

    Logger log = LoggerFactory.getLogger(KPIsService.class);

    private final HistoryService storeHistory;

    public KPIsService(HistoryService storeHistory) {
        this.storeHistory = storeHistory;
    }

    public List<KPIsCA> getKPIsCAEvolution(Long storeId){
        List<KPIsCA> kpisList = new ArrayList<>();
        log.info("getting KPIs of CA in store Id {}", storeId);
        double previousCA = 0;
        for (Map.Entry<YearMonth, Double> entry : storeHistory.getPurchaseHistoryPerMonth(storeId).entrySet()) {
            YearMonth monthYear = entry.getKey();
            double currentCA = entry.getValue();
            double percentage = (previousCA != 0) ? ((currentCA - previousCA) / previousCA) * 100 : 0;
            KPIsCA kpi = new KPIsCA(monthYear, currentCA, Double.parseDouble(String.format(Locale.US, "%.2f", percentage)));
            kpisList.add(kpi);
            previousCA = currentCA;
        }
        return kpisList;
    }

    public List<KPIsOffers> getKPIsGiftsEvolution(Long storeId){
        List<KPIsOffers> kpisList = new ArrayList<>();
        log.info("getting KPIs of Gifts in store Id {}", storeId);
        double previousCA = 0;
        for (Map.Entry<YearMonth, Integer> entry : storeHistory.getGiftsHistoryPerMonth(storeId).entrySet()) {
            YearMonth monthYear = entry.getKey();
            int currentCA = entry.getValue();
            double percentage = (previousCA != 0) ? ((currentCA - previousCA) / previousCA) * 100 : 0;
            KPIsOffers kpi = new KPIsOffers(monthYear, currentCA, Double.parseDouble(String.format(Locale.US,"%.2f", percentage)));
            kpisList.add(kpi);
            previousCA = currentCA;
        }
        return kpisList;
    }

}