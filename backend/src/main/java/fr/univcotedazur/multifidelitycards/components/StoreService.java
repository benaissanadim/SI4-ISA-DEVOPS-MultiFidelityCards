package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.connectors.NotifierProxy;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.controllers.dto.ScheduleDTO;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.*;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * StoreService to manage Store
 */
@Transactional
@Component
public class StoreService implements IStoreManager, IStoreExplorator, IStoreFinder {

    Logger log = LoggerFactory.getLogger(StoreService.class);

    private final StoreRepository storeRepository;
    private final ClientFinder clientFinder;
    private final GeographicZoneFinder geographicZoneFinder;
    private final NotifierProxy notifierProxy;


    @Autowired // annotation is optional since Spring 4.3 if component has only one constructor
    public StoreService(StoreRepository storeRepository, GeographicZoneFinder geographicZoneFinder,
            ClientFinder clientFinder, NotifierProxy notifierProxy) {
        this.storeRepository = storeRepository;
        this.geographicZoneFinder = geographicZoneFinder;
        this.clientFinder = clientFinder;
        this.notifierProxy = notifierProxy;
    }

    public Store register(String name, String address, String zone)
            throws GeographicZoneNotFoundException {
        GeographicZone zoneGeo = this.geographicZoneFinder.findByName(zone);
        Store store = new Store(name, address,new ArrayList<>(), zoneGeo);
        this.storeRepository.save(store);
        return store;
    }

    public Store addSchedule(Long storeId, ScheduleDTO scheduleDTO) {
        String dayOfWeek = scheduleDTO.getDayOfWeek();
        String beginTime = scheduleDTO.getBeginTime();
        String endTime = scheduleDTO.getEndTime();

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + storeId));

        Optional<Schedule> scheduleOptional = store.getSchedule().stream()
                .filter(s -> s.getDayOfWeek().equals(dayOfWeek))
                .findFirst();
        Schedule schedule;
        if(scheduleOptional.isPresent()) {
            log.info("Editing schedule for store {} on {}", store.getName(), dayOfWeek);
            schedule = scheduleOptional.get();
            schedule.setBeginTime(beginTime);
            schedule.setEndTime(endTime);
        }else{
            schedule = new Schedule(beginTime, endTime, dayOfWeek);
            log.info("Adding schedule for store {} on {}", store.getName(), dayOfWeek);
            store.addSchedule(schedule);
        }
        storeRepository.save(store);
        notifierProxy.notifyFavouriteStore(store, schedule);
        log.info("store {} schedule updated", store.getName());
        return store;
    }

    @Override
    public List<Store> consultStoresperZone(String zone) {
        return storeRepository.findByZone_Name(zone);
    }

    @Override
    public List<Store> consultAll() {
        return storeRepository.findAll();
    }

    @Override
    public Store findById(Long id) throws StoreNotFoundException {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isEmpty()) {
            String msg = "store with id "+id+" not found";
            log.warn(msg);
            throw new StoreNotFoundException(msg);
        }
        return optionalStore.get();
    }

    @Override
    public Store findByName(String name) throws StoreNotFoundException {
        Optional<Store> optionalStore = storeRepository.findStoreByName(name);
        if (optionalStore.isEmpty()) {
            String msg = "store with name "+name+" not found";
            log.warn(msg);
            throw new StoreNotFoundException(msg);
        }
        return optionalStore.get();
    }

    @Override
    public boolean addFavoriteStore(Long storeId, Long clientId) {
        try {
            Store store = findById(storeId);
            Client client = clientFinder.findById(clientId);
            log.info("adding client to store");
            store.addToFavourite(client);
            storeRepository.save(store);
            return true;
        }catch (Exception e){
            log.warn("error while adding client to store");
            return false;
        }

    }

    @Override public void deleteStore(Long storeId) {
        storeRepository.deleteById(storeId);
        log.info("store with id {} deleted", storeId);
    }
}
