package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FidelityCardRepository extends JpaRepository<FidelityCard, Long> {

    Optional<FidelityCard> findFidelityCardByClient_Id(Long id);


}
