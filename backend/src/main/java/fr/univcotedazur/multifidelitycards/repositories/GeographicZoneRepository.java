package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeographicZoneRepository extends JpaRepository<GeographicZone, Long> {
    Optional<GeographicZone> findByName(String name);
}