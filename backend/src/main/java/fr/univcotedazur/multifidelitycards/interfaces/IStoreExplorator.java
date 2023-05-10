package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Store;

import java.util.List;

/**
 * store explorator
 */
public interface IStoreExplorator {

    List<Store> consultStoresperZone(String zone);
    List<Store> consultAll();
}
