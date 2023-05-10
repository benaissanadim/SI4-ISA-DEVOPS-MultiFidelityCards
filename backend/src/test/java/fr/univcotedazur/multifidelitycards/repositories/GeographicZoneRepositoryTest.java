package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class GeographicZoneRepositoryTest {
    @Autowired
    GeographicZoneRepository geographicZoneRepository;
    GeographicZone geographicZone;

    public GeographicZoneRepositoryTest() {
    }

    @BeforeEach void setup() {
        System.out.println(this.geographicZoneRepository);;
        this.geographicZoneRepository.deleteAll();
        this.geographicZone = new GeographicZone("antibes");
    }

    @Test
    void testSaveAndFind() {
        this.geographicZoneRepository.save(this.geographicZone);
        Long genId = this.geographicZone.getId();
        Assertions.assertNotNull(genId);
        Optional<GeographicZone> foundOpt = this.geographicZoneRepository.findById(genId);
        Assertions.assertTrue(foundOpt.isPresent());
        Assertions.assertEquals(this.geographicZone, foundOpt.get());
    }

    @Test void testDeleteAll() {
        Assertions.assertEquals(0L, this.geographicZoneRepository.count());
        this.geographicZoneRepository.save(this.geographicZone);
        Assertions.assertEquals(1L, this.geographicZoneRepository.count());
        this.geographicZoneRepository.deleteAll();
        Assertions.assertEquals(0L, this.geographicZoneRepository.count());
    }

}