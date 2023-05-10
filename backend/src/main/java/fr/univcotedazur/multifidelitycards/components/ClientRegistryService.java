package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingClientException;
import fr.univcotedazur.multifidelitycards.exceptions.ClientNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.ClientFinder;
import fr.univcotedazur.multifidelitycards.interfaces.ClientRegistration;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ClientRegistryService allows to register a new client and to find a client
 * by its username or its email.
 */
@Transactional
@Component
public class ClientRegistryService implements ClientRegistration, ClientFinder {

    Logger log = LoggerFactory.getLogger(ClientRegistryService.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientRegistryService(ClientRepository customerRepository) {
        this.clientRepository = customerRepository;
    }

    @Override
    public Client register(String username, String email) throws AlreadyExistingClientException {
        String msg;
        log.debug("registering client with username {} and email {}",username, email);
        if (clientRepository.findClientByUsername(username).isPresent()) {
            msg = "client with username "+username+" already exists";
            log.warn(msg);
            throw new AlreadyExistingClientException(msg);
        }
        if (clientRepository.findClientByEmail(email).isPresent()) {
            msg = "client with email "+email+" already exists";
            log.warn(msg);
            throw new AlreadyExistingClientException(msg);
        }
        Client customer = new Client(username, email);
        clientRepository.save(customer);
        log.info("client with username {} and email {} registered",username, email);
        return customer;
    }

    @Override
    public Client findById(Long id) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            String msg = "client with id "+id+" not found";
            log.warn(msg);
            throw new ClientNotFoundException(msg);
        }
        return client.get();
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

}
