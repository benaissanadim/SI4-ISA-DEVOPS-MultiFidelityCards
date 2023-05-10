package fr.univcotedazur.multifidelitycards.cucumber.client;

import fr.univcotedazur.multifidelitycards.components.ClientRegistryService;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientTest {

    @Autowired
    private ClientRegistryService registry;

    @Autowired
    private ClientRepository repository;

    private String msg;
    private Client client;

    @Given("given users {string} email {string} and {string} email {string}")
    public void givenUsersEmailAndEmail(String arg0, String arg1, String arg2, String arg3) {
        repository.deleteAll();
        Client c1 = new Client(arg0, arg1);
        Client c2 = new Client(arg2, arg3);
        repository.save(c1);
        repository.save(c2);
    }

    @When("user enters email {string} and username {string}")
    public void userEntersEmailEmailAndUsernameUsername(String email, String username) {
        try {
            client = registry.register(username,email);
        }catch (Exception e){
            msg = e.getMessage();
        }

    }

    @Then("exception is {string}")
    public void exceptionIsMsg(String arg0) {
        Assertions.assertEquals(arg0,msg);
    }

    @Then("a new user is registered")
    public void aNewUserIsRegistered() {
        Assertions.assertNotNull(client);
    }

}