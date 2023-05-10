package fr.univcotedazur.multifidelitycards.components;


import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneAdder;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneFinder;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *  GeographicZoneService allows to add a new geographic zone
 *  and to find a geographic zone by its name or its id.
 */
@Transactional
@Component
public class GeographicZoneService implements GeographicZoneFinder, GeographicZoneAdder {
    Logger log = LoggerFactory.getLogger(GeographicZoneService.class);

    private final GeographicZoneRepository geographicZoneRepository;

    @Autowired
    public GeographicZoneService(GeographicZoneRepository geographicZoneRepository) {
        this.geographicZoneRepository = geographicZoneRepository;
    }

    public GeographicZone findByName(String name) throws GeographicZoneNotFoundException {
        Optional<GeographicZone> optZone = geographicZoneRepository.findByName(name);
        String msg = "Geographic zone " + name + " does not exists";
        if(optZone.isEmpty()){
            log.warn(msg);
            throw new GeographicZoneNotFoundException(msg);
        }
        return optZone.get();
    }

    public GeographicZone findById(Long id) throws GeographicZoneNotFoundException {
        Optional<GeographicZone> optZone = geographicZoneRepository.findById(id);
        String msg = "Geographic zone with id " + id + " does not exists";
        if(optZone.isEmpty()){
            log.warn(msg);
            throw new GeographicZoneNotFoundException(msg);
        }
        return optZone.get();
    }

    @Override public List<GeographicZone> findAll() {
        return geographicZoneRepository.findAll();
    }

    public GeographicZone add(String name) throws AlreadyExistingGeographicZoneException {
        if (geographicZoneRepository.findByName(name).isPresent()) {
            log.warn("Geographic zone {} already exists", name);
            throw new AlreadyExistingGeographicZoneException("Geographic zone " + name + " already exists");
        }
        GeographicZone geographicZone = new GeographicZone(name);
        geographicZoneRepository.save(geographicZone);
        log.info("Geographic zone {} added", name);
        return geographicZone;
    }
}
