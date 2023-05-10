package fr.univcotedazur.multifidelitycard.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextStore;
import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import fr.univcotedazur.multifidelitycard.cli.model.CliSchedule;
import fr.univcotedazur.multifidelitycard.cli.model.CliStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(StoreCommands.class)
public class StoreCommandsTest {
    @Autowired
    private StoreCommands storeCommands;

    @Autowired
    private RestTemplate restTemplate;
    @MockBean
    private CliContextStore cliContext;
    @MockBean
    private CliClientContext cliClientContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockRestServiceServer mockServer;

    @MockBean
    private CliStore cliStore;

    private String baseUri = "/stores";
    String l = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testRegisterStore() throws Exception {
        CliStore expectedStore = new CliStore(1L, "Store1", "Address1", null, "Zone1");
        mockServer.expect(requestTo(l+baseUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedStore), MediaType.APPLICATION_JSON));

        CliStore actualStore = storeCommands.registerStore("Store1", "Address1", "Zone1");

        mockServer.verify();
        assertEquals(expectedStore.getName(), actualStore.getName());
    }
    @Test
    public void testEditStoreSchedule() throws Exception {

        List<CliStore> stores = new ArrayList<>();
        CliStore store1 = new CliStore(1L, "Store1", "Address1", null, "Zone1");
        CliStore store2 = new CliStore(2L, "Store2", "Address2", null, "Zone2");
        stores.add(store1);
        stores.add(store2);




        CliSchedule expectedSchedule = new CliSchedule(1L, "9:00", "17:00", "Monday");
        mockServer.expect(requestTo(baseUri + "/Store1/schedule"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.beginTime").value("9:00"))
                .andExpect(jsonPath("$.endTime").value("17:00"))
                .andExpect(jsonPath("$.dayOfWeek").value("Monday"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedSchedule), MediaType.APPLICATION_JSON));


        Map<String, CliStore> storeMap = new HashMap<>();
        storeMap.put("Store1", store1);
        when(cliContext.getStoreMap()).thenReturn(storeMap);

        mockServer.expect(requestTo(l+baseUri+"/1/schedule"))
                .andExpect(method(HttpMethod.PUT));



    }
    @Test
    public void testAddToFavourite() throws Exception {

        String expectedResponse = "Success";
        mockServer.expect(requestTo(l + baseUri + "/1/favourites/client/1"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedResponse, MediaType.TEXT_PLAIN));

        when(cliContext.getStoreMap()).thenReturn(Collections.singletonMap("Store1", new CliStore()));
        cliContext.getStoreMap().get("Store1").setId(1L);
        when(cliClientContext.getCustomers()).thenReturn(Collections.singletonMap("Client1", new CliClient()));
        cliClientContext.getCustomers().get("Client1").setId(1L);

        String result = storeCommands.addToFavourite("Store1", "Client1");

        mockServer.verify();
        assertEquals(expectedResponse, result);
    }
    @Test
    public void testGetFavouriteClients() throws Exception {
        when(cliContext.getStoreMap()).thenReturn(Collections.singletonMap("Store1", new CliStore()));
        cliContext.getStoreMap().get("Store1").setId(1L);
        String expectedResponse = "Client1, Client2";
        mockServer.expect(requestTo(l + baseUri + "/1/favourites"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, MediaType.TEXT_PLAIN));

        String result = storeCommands.getFavouriteClients("Store1");

        mockServer.verify();
        assertEquals(expectedResponse, result);
    }
    @Test
    public void testListStores() {

        String expectedResponse = "Store1, Store2";
        when(cliContext.toString()).thenReturn(expectedResponse);
        String result = storeCommands.listStores();

        assertEquals(expectedResponse, result);
    }




}
