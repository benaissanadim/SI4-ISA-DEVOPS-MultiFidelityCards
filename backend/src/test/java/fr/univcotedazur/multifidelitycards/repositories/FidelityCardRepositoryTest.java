package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
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
class FidelityCardRepositoryTest {
    @Autowired
    FidelityCardRepository fidelityCardRepository;
    FidelityCard card;
    Client client;

    FidelityCardRepositoryTest() {
    }

    @BeforeEach
    void setup() {
        this.fidelityCardRepository.deleteAll();
        this.client = new Client("john", "john@gmail.com");
        GeographicZone zone = new GeographicZone("antibes");
        this.card = new FidelityCard(client, zone);
    }

    @Test
    void testSaveAndFind() {
        this.fidelityCardRepository.save(this.card);
        Long genId = this.card.getId();
        Optional<FidelityCard> foundOpt = this.fidelityCardRepository.findById(genId);
        Assertions.assertTrue(foundOpt.isPresent());
        Assertions.assertEquals(this.card, foundOpt.get());
    }

}
