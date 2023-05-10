package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.controllers.dto.ScheduleDTO;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;

/**
 * store manager interface
 */
public interface IStoreManager {

    Store addSchedule(Long storeId, ScheduleDTO scheduleDTO) ;
    Store register(String name, String address, String zone)
            throws GeographicZoneNotFoundException;
    boolean addFavoriteStore(Long storeId, Long clientId);
    void deleteStore(Long storeId);

}
