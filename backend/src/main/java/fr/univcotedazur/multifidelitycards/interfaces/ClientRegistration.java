package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingClientException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;

/**
 * ClientRegistration intefrace
 */
public interface ClientRegistration {
    /**
     Registers a new client with the given username and email address.
     @param username the desired username for the new client
     @param email the email address for the new client
     @return the newly registered client
     @throws AlreadyExistingClientException if a client with the same username or email already exists
     */
    Client register(String username, String email) throws AlreadyExistingClientException;

}
