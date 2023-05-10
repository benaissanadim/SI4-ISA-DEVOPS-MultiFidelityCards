package fr.univcotedazur.multifidelitycard.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextZone;
import fr.univcotedazur.multifidelitycard.cli.model.CliZoneGeographic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@RestClientTest(ZoneGeograpicCommands.class)
public class ZoneGeographicCommandsTest {
    @Autowired
    private ZoneGeograpicCommands zoneGeograpicCommands;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockRestServiceServer mockServer;

    @MockBean
    private CliContextZone cliContextZone;

    String l = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockServer.reset();
    }

    @Test
    public void testAddZone() throws Exception {
        String zoneName = "Zone1";
        CliZoneGeographic expectedZone = new CliZoneGeographic(zoneName);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedZone);

        mockServer.expect(MockRestRequestMatchers.requestTo(ZoneGeograpicCommands.BASE_URI ))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(expectedZone)))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        CliZoneGeographic actualZone = zoneGeograpicCommands.add(zoneName);

        Assertions.assertEquals(expectedZone.getName(), actualZone.getName());
        mockServer.verify();


    }

    @Test
    public void testListZones() throws Exception {
        String expectedResponseBody = "[{\"zoneName\":\"Zone1\"},{\"zoneName\":\"Zone2\"}]";

        mockServer.expect(MockRestRequestMatchers.requestTo(ZoneGeograpicCommands.BASE_URI))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        String actualOutput = zoneGeograpicCommands.listZones();


    }


}
