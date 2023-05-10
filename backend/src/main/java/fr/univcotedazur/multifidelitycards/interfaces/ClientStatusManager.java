package fr.univcotedazur.multifidelitycards.interfaces;

import fr.univcotedazur.multifidelitycards.entities.Client;

/**
 * Client staus manager
 */
public interface ClientStatusManager {
    /**
     * change status
     * @param client client
     */
    void changeStatus(Client client);
}
