package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.exceptions.FidelityCardNotFoundException;

import java.util.List;

/**
 * FidelityCardFinder
 */
public interface FidelityCardFinder {
    /**
     * find fidelty card by id
     * @param id id of fidelity card
     * @return card
     * @throws FidelityCardNotFoundException exception of not found
     */
    FidelityCard findById(Long id) throws FidelityCardNotFoundException;

    /**
     * find all
     * @return list of fidelity cards
     */
    List<FidelityCard> findAll();
}
