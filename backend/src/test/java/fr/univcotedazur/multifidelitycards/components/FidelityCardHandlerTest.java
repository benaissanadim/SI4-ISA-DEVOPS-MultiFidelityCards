package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingFidelityCardException;
import fr.univcotedazur.multifidelitycards.exceptions.ClientNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.NotEnoughPointsException;
import fr.univcotedazur.multifidelitycards.interfaces.FidelityCardCreator;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class FidelityCardHandlerTest {

     @Autowired
    private FidelityCardCreator fidelityCardCreator;
    @Autowired
    private FidelityCardRepository fidelityCardRepository;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FidelityCardHandlerService fidelityCardHandlerService;

    private Client client;
    private GeographicZone zone;
    private FidelityCard card;

    @BeforeEach
    void setUp() {
        this.geographicZoneRepository.deleteAll();
        this.fidelityCardRepository.deleteAll();
        this.client = new Client("john", "john@gmail.com");
        this.zone = new GeographicZone("antibes");
        this.card = new FidelityCard(this.client, this.zone);
    }

     @Test
     public void createCardSuccess() throws Exception {
         FidelityCard card = new FidelityCard(this.client, this.zone);
         this.clientRepository.save(client);
         System.out.println(clientRepository.count());
         this.geographicZoneRepository.save(zone);
         this.fidelityCardCreator.create(this.client.getId(), this.zone.getName());
         Assertions.assertEquals(1, fidelityCardRepository.count());
         List<FidelityCard> fidelityCards = fidelityCardRepository.findAll();
         FidelityCard cardReturned = fidelityCards.get(0);
         Assertions.assertEquals(cardReturned.getClient(), card.getClient());
         Assertions.assertEquals(cardReturned.getGeographicZone(), card.getGeographicZone());
     }

    @Test
    public void createCardFailedZone() throws Exception {
        this.clientRepository.save(this.client);
        Assertions.assertThrows(GeographicZoneNotFoundException.class, () -> {
            this.fidelityCardCreator.create(this.client.getId(), this.zone.getName());
        });
    }

    @Test
    public void createCardFailedClient() {
        this.geographicZoneRepository.save(this.zone);
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            this.fidelityCardCreator.create(100L, this.zone.getName());
        });
    }

    @Test
    public void createCardNotTwice() throws Exception {
        this.clientRepository.save(this.client);
        this.geographicZoneRepository.save(this.zone);
        this.fidelityCardCreator.create(this.client.getId(), this.zone.getName());
        Assertions.assertThrows(AlreadyExistingFidelityCardException.class, () -> {
            this.fidelityCardCreator.create(this.client.getId(), this.zone.getName());
        });
    }

    @Test
    public void testPoints() throws Exception {
        String msg ="";
        clientRepository.save(client);
        geographicZoneRepository.save(zone);
        FidelityCard f = fidelityCardCreator.create(this.client.getId(), this.zone.getName());
        try{
            fidelityCardHandlerService.retrievePoints(f,200);
        }catch (NotEnoughPointsException e){
            msg = e.getMessage();
        }
        Assertions.assertEquals("not enough points to retrieve", msg);
    }



}
