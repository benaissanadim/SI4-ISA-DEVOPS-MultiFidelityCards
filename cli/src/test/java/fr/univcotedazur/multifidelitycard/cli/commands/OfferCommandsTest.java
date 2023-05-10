package fr.univcotedazur.multifidelitycard.cli.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycard.cli.context.*;
import fr.univcotedazur.multifidelitycard.cli.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OfferCommands.class)
public class OfferCommandsTest {
    @Autowired
    private OfferCommands offerCommands;
    @MockBean
    private CliContextOffer cliContext;
    @MockBean
    private CliContextGift cliContextGift;
    @MockBean
    private CliContextCollectiveOffer cliContextCollectiveOffer;
    @MockBean
    private CliClientContext cliClientContext;
    @MockBean
    private CliContextStore cliContextStore;
    @MockBean
    private CliContextZone cliContextZone;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Autowired
    private MockRestServiceServer mockServer;

    String l = "http://localhost:8080";


    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    @Test
    public void testAddGift() throws Exception {
        when(cliContextStore.getStoreMap()).thenReturn(Collections.singletonMap("Store1", new CliStore()));
        cliContextStore.getStoreMap().get("Store1").setId(1L);
        CliGift expectedGift = new CliGift("Gift1", "Description1", 100, "Store1", "Classic_Offer");
        String expectedResponse = objectMapper.writeValueAsString(expectedGift);
        mockServer.expect(requestTo(l+offerCommands.BASE_URI + "/store/1/"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        CliGift actualGift = offerCommands.addGift("Gift1", "Description1", 100, "Store1", "Classic_Offer");


        mockServer.verify();
        assertEquals(expectedGift.getStore(), actualGift.getStore());
        assertEquals(expectedGift.getName(), actualGift.getName());
        assertEquals(expectedGift.getDescription(), actualGift.getDescription());
        assertEquals(expectedGift.getPoints(), actualGift.getPoints());
    }
    @Test
    public void testAddCollectiveOffer() throws JsonProcessingException {
        when(cliContextZone.getZoneGeographic()).thenReturn(Collections.singletonMap("Test Zone", new CliZoneGeographic()));
        cliContextZone.getZoneGeographic().get("Test Zone").setId(1L);
        CliParkingOffer expectedOffer = new CliParkingOffer("Test Offer", "Test Description", 200, "Test Zone", "CLASSIC_OFFER", 30);
        String expectedResponse = objectMapper.writeValueAsString(expectedOffer);
        mockServer.expect(requestTo(l + "/offers/zone/1/"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));


        CliParkingOffer actualOffer = offerCommands.addCollectiveOffer("Test Offer", "Test Description", 200, "Test Zone", "CLASSIC_OFFER", 30);


        mockServer.verify();

        assertNotNull(actualOffer);
        assertEquals(expectedOffer.getName(), actualOffer.getName());
        assertEquals(expectedOffer.getDescription(), actualOffer.getDescription());
        assertEquals(expectedOffer.getPoints(), actualOffer.getPoints());
        assertEquals(expectedOffer.getGeographicZone(), actualOffer.getGeographicZone());

    }
}
