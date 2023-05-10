package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;

/**
 * survey finder
 */
public interface IStoreFinder {

    Store findById(Long id) throws StoreNotFoundException;
    Store findByName(String name) throws StoreNotFoundException;
}