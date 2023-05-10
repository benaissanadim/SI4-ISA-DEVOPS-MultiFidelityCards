package fr.univcotedazur.multifidelitycard.cli.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestClientTest(ClientCommands.class)
public class ClientCommandsTest {

    @Autowired
    private ClientCommands clientCommands;
    @MockBean
    private CliClientContext cliContext;
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
    public void testRegister() throws Exception {
        String name = "John Doe";
        String email = "john.doe@example.com";
        CliClient expectedClient = new CliClient(name, email);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedClient);

        mockServer.expect(MockRestRequestMatchers.requestTo(l+ClientCommands.BASE_URI))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(expectedClient)))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        CliClient actualClient = clientCommands.register(name, email);

        Assertions.assertEquals(expectedClient, actualClient);
        mockServer.verify();
    }
    @Test
    public void testListClients() throws Exception {
        List<CliClient> expectedClients = Arrays.asList(
                new CliClient("John Doe", "john.doe@example.com"),
                new CliClient("Jane Smith", "jane.smith@example.com")
        );
        String expectedResponseBody = objectMapper.writeValueAsString(expectedClients);

        mockServer.expect(MockRestRequestMatchers.requestTo(l + ClientCommands.BASE_URI))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        String actualOutput = clientCommands.listClients();

        Assertions.assertEquals(cliContext.toString(), actualOutput);
        mockServer.verify();
    }


}
