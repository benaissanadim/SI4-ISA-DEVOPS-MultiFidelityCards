package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingFidelityCardException;
import fr.univcotedazur.multifidelitycards.exceptions.ClientNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;

/**
 * Fidelitycard creator
 */
public interface FidelityCardCreator {
    /**
     *
     * @param clientID id of client
     * @param zoneName zone name
     * @return card
     * @throws ClientNotFoundException client not found
     * @throws AlreadyExistingFidelityCardException already having card
     * @throws GeographicZoneNotFoundException geographic zone not found
     */
     FidelityCard create(Long clientID, String zoneName) throws ClientNotFoundException,
            AlreadyExistingFidelityCardException, GeographicZoneNotFoundException ;

    }
