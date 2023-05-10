package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findSurveyById(Long id);

}
