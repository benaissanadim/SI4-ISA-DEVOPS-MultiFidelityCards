package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingClientException;
import fr.univcotedazur.multifidelitycards.interfaces.ClientFinder;
import fr.univcotedazur.multifidelitycards.interfaces.ClientRegistration;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class ClientRegistryServiceTest {
    @Autowired
    private ClientRepository customerRepository;
    @Autowired
    private ClientRegistration customerRegistration;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;

    @Autowired
    private ClientFinder customerFinder;

    private String name = "John";
    private String email = "jhon@gmail.com";
    private final String zoneName = "Nice Cote d'Azur";
    private GeographicZone zone = new GeographicZone(zoneName);

    ClientRegistryServiceTest() {
    }

    @BeforeEach
    void setUp() {
        geographicZoneRepository.save(zone);
    }

    @Test
    public void unknownCustomer() {
        Assertions.assertFalse(this.customerRepository.findClientByUsername(name).isPresent());
    }

    @Test
    public void registerCustomer() throws Exception {
        Client returned = this.customerRegistration.register(name,email);
        Optional<Client> customer = this.customerRepository.findClientByUsername(this.name);
        Assertions.assertTrue(customer.isPresent());
        Client john = (Client)customer.get();
        Assertions.assertEquals(john, returned);
        Assertions.assertEquals(john, this.customerFinder.findById(returned.getId()));
        Assertions.assertEquals(this.name, john.getUsername());
    }

    @Test
    public void cannotRegisterTwiceEmail() throws Exception {
        this.customerRegistration.register("new", this.email);
        Assertions.assertThrows(AlreadyExistingClientException.class, () -> {
            this.customerRegistration.register(this.name, this.email);
        });
    }

    @Test
    public void cannotRegisterTwiceUsername() throws Exception {
        this.customerRegistration.register(this.name, "email@gmail.com");
        Assertions.assertThrows(AlreadyExistingClientException.class, () -> {
            this.customerRegistration.register(this.name, this.email);
        });
    }

}
