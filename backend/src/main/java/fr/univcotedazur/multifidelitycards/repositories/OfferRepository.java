package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query(nativeQuery = true, value = "SELECT offer.* FROM offer left "
            + "join store on offer.store_id = store.id "
            + "where offer.zone_id = ?1  or store.zone_id = ?1")
    List<Offer> findOfferByGeographicZoneId(Long id);

}
