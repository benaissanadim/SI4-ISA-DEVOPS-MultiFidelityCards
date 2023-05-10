package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.OfferUseAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferUseRepository extends JpaRepository<OfferUseAction, Long> {

    List<OfferUseAction> findAll();

}