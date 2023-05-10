package fr.univcotedazur.multifidelitycards.cucumber.fidelitycard;

import fr.univcotedazur.multifidelitycards.components.FidelityCardHandlerService;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingFidelityCardException;
import fr.univcotedazur.multifidelitycards.exceptions.ClientNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FidelityCardCreateTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    FidelityCardRepository fidelityCardRepository;
    @Autowired
    GeographicZoneRepository geographicZoneRepository;
    @Autowired FidelityCardHandlerService fidelityCardService;

    Client client;
    GeographicZone zone;
    FidelityCard fidelityCard;
    String msg;

    @Before
    public void setUpContext(){
        clientRepository.deleteAll();
        geographicZoneRepository.deleteAll();
    }

    @Given("given client {string} email {string}")
    public void givenClientEmail(String arg0, String arg1) {
        client = new Client(arg0, arg1);
        clientRepository.save(client);
    }

    @And("zone {string}")
    public void zone(String arg0) {
        zone = new GeographicZone(arg0);
        geographicZoneRepository.save(zone);
    }

    @When("client {string} creates new card in {string}")
    public void clientCreatesNewCardIn(String arg0, String arg1)
            throws ClientNotFoundException, AlreadyExistingFidelityCardException {
        try{
            fidelityCard = fidelityCardService.create(client.getId(), arg1);
        }catch (GeographicZoneNotFoundException e){
            msg = e.getMessage();
        }
    }

    @Then("card created successfully")
    public void cardCreatedSuccessfully() {
        Assertions.assertNotNull(fidelityCardService);
    }

    @Then("exception {string}") public void exceptionMsg(String arg) {
        Assertions.assertEquals(arg, msg);
    }

    @And("client can't create another card")
    public void clientCanTCreateAnotherCard() {
        Assertions.assertThrows(AlreadyExistingFidelityCardException.class, ()->
                fidelityCardService.create(client.getId(), zone.getName()));
    }
}
