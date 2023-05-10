package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.exceptions.ClientNotFoundException;

import java.util.List;

/**
 * This interface defines methods for finding clients.
 */
public interface ClientFinder {

    /**

     Finds a client by ID.
     @param id the ID of the client to find
     @return the client with the given ID
     @throws ClientNotFoundException if no client with the given ID exists
     */
    Client findById(Long id) throws ClientNotFoundException;
    /**

     Finds all clients.
     @return a list of all clients
     */
    List<Client> findAll();
}
