package fr.univcotedazur.multifidelitycards.cucumber.geographiczone;

import fr.univcotedazur.multifidelitycards.components.GeographicZoneService;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GeographicZoneCreateTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    FidelityCardRepository fidelityCardRepository;
    @Autowired
    GeographicZoneRepository geographicZoneRepository;
    @Autowired GeographicZoneService geographicZoneService;

    GeographicZone zone;
    String msg;

    @Before
    public void setUpContext(){
        geographicZoneRepository.deleteAll();
    }


    @When("admin add new zone {string}")
    public void adminAddNewZone(String arg0) throws AlreadyExistingGeographicZoneException {
        zone = geographicZoneService.add(arg0);
    }

    @Then("number of zones is now {int}")
    public void numberOfZonesIsNow(int arg0) {
        Assertions.assertEquals(arg0, geographicZoneRepository.count());
    }

    @And("admin can't add the zone {string} again")
    public void adminCanTAddTheZoneAgain(String arg0) {

    }

    @And("When admin try to add again the same zone {string}")
    public void whenAdminTryToAddAgainTheSameZone(
            String arg0) {
        try {
            geographicZoneService.add(arg0);
        }catch (AlreadyExistingGeographicZoneException e){
            msg = e.getMessage();
        }
    }
}
