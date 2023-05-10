package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.KPIsCA;
import fr.univcotedazur.multifidelitycards.entities.KPIsOffers;

import java.util.List;

/**
 * store KPIs
 */
public interface IStoreKPIs {
    List<KPIsCA> getKPIsCAEvolution(Long storeId);
    List<KPIsOffers> getKPIsGiftsEvolution(Long storeId);
}
