package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneAdder;
import fr.univcotedazur.multifidelitycards.interfaces.GeographicZoneFinder;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class GeographicZoneServiceTest {
    @Autowired
    private GeographicZoneAdder geographicZoneAdder;
    @Autowired
    private GeographicZoneFinder geographicZoneFinder;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;
    private String name = "nice cote d'azure";

    GeographicZoneServiceTest() {
    }

    @BeforeEach
    void setUp() {
        this.geographicZoneRepository.deleteAll();
    }


    @Test
    public void addZone() throws Exception {
        GeographicZone returned = this.geographicZoneAdder.add(this.name);
        Optional<GeographicZone> zone = this.geographicZoneRepository.findByName(this.name);
        Assertions.assertTrue(zone.isPresent());
        GeographicZone zoneReturned = (GeographicZone)zone.get();
        Assertions.assertEquals(zoneReturned, returned);
        Assertions.assertEquals(zoneReturned, this.geographicZoneRepository.findById(returned.getId()).get());
        Assertions.assertEquals(this.name, zoneReturned.getName());
    }

    @Test
    public void cannotAddZoneTwice() throws Exception {
        this.geographicZoneAdder.add(this.name);
        Assertions.assertThrows(AlreadyExistingGeographicZoneException.class, () -> {
            this.geographicZoneAdder.add(this.name);
        });
    }
}
