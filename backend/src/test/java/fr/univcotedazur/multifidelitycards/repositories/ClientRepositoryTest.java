package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class ClientRepositoryTest {
    @Autowired
    ClientRepository customerRepository;
    Client john;

    ClientRepositoryTest() {
    }


    @BeforeEach
    void setup() {
        System.out.println(customerRepository);
        this.customerRepository.deleteAll();
        this.john = new Client("john", "john@gmail.com");
    }

    @Test
    void testSaveAndFind() {
        this.customerRepository.save(this.john);
        Long genId = this.john.getId();
        Optional<Client> foundJohnOpt = this.customerRepository.findById(genId);
        Assertions.assertTrue(foundJohnOpt.isPresent());
        Assertions.assertEquals(this.john, foundJohnOpt.get());
    }

    @Test
    void testDeleteAll() {
        Assertions.assertEquals(0L, this.customerRepository.count());
        this.customerRepository.save(this.john);
        Assertions.assertEquals(1L, this.customerRepository.count());
        this.customerRepository.deleteAll();
        Assertions.assertEquals(0L, this.customerRepository.count());
    }


}