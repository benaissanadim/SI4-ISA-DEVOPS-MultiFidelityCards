package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;

/**
 * GeographicZoneAdder interface
 */
public interface GeographicZoneAdder {
    /**
     * add zone
     * @param name name
     * @return zone
     * @throws AlreadyExistingGeographicZoneException exception
     */
    GeographicZone add(String name) throws AlreadyExistingGeographicZoneException;

}
