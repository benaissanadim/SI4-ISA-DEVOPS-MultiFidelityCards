package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;

import java.util.List;

/**
 * GeographiczoneFinder inteface
 */
public interface GeographicZoneFinder {
    /**
     *
     * @param name name
     * @return zone
     * @throws GeographicZoneNotFoundException exception
     */
    GeographicZone findByName(String name) throws GeographicZoneNotFoundException;
    GeographicZone findById(Long id) throws GeographicZoneNotFoundException;
    List<GeographicZone> findAll();
}
