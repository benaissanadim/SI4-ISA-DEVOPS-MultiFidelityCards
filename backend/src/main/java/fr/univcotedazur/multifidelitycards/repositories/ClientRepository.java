package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {

    Optional<Client> findClientByUsername(String name);
    Optional<Client> findClientByEmail(String email);
    Optional<Client> findClientById(Long id);

}
