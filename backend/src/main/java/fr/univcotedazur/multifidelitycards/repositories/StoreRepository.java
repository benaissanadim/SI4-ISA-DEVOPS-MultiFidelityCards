package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findStoreByName(String name);
    List<Store> findByZone_Name(String zoneName);
}
