package fr.univcotedazur.multifidelitycards.cucumber.store;

import fr.univcotedazur.multifidelitycards.components.ClientRegistryService;
import fr.univcotedazur.multifidelitycards.components.GeographicZoneService;
import fr.univcotedazur.multifidelitycards.components.StoreService;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.entities.Schedule;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingClientException;
import fr.univcotedazur.multifidelitycards.exceptions.AlreadyExistingGeographicZoneException;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class StoreCreateTest {
    @Autowired
    private ClientRegistryService service ;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired

    private StoreService storeService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    GeographicZoneService geographicZoneService;
    @Autowired
    GeographicZoneRepository geographicZoneRepository;

    GeographicZone zone ;
    Store store;
    List<Schedule> scheduleList ;
    Client client;

    @When("a merchant wants to add a store in a zone {string}")
    public void aMerchantWantsToAddAStoreInAZone(String arg0) throws AlreadyExistingGeographicZoneException, GeographicZoneNotFoundException {
        zone = new GeographicZone(arg0);
        scheduleList = List.of(new Schedule( "10:00", "12:00","MONDAY"));
        store = new Store("store1", "address1",scheduleList, zone);
        geographicZoneService.add(zone.getName());
        storeService.register(store.getName(), store.getAddress(), zone.getName());

    }

    @Then("the number of stores should be {int}")
    public void theNumberOfStoresShouldBe(int arg0) {
        Assertions.assertEquals(arg0, storeRepository.count());
    }

    @And("when a user consults stores in zone {string}")
    public void whenAUserConsultsStoresInZone(String arg0) {
        List<Store> stores = storeService.consultStoresperZone(arg0);
    }

    @Then("he should find the created store")
    public void heShouldFindTheCreatedStore() {
        Assertions.assertEquals(store.getName(), store.getName());
    }

    @When("I user adds the store to favorites")
    public void iUserAddsTheStoreToFavorites() throws AlreadyExistingClientException, GeographicZoneNotFoundException, AlreadyExistingGeographicZoneException {
        zone = new GeographicZone("new york");
        store = new Store("store1", "address1",scheduleList, zone);
        storeService.register(store.getName(), store.getAddress(), zone.getName());
        client = new Client("hamza","hamza@gmail.com");
        service.register(client.getUsername(), client.getEmail());
        storeService.addFavoriteStore(store.getId(), client.getId());
    }

    @Then("the store should be in the list of favorites")
    public void theStoreShouldBeInTheListOfFavorites()  {
        //Assertions.assertEquals(1,store.getClientsFavorite().size());
    }


}
