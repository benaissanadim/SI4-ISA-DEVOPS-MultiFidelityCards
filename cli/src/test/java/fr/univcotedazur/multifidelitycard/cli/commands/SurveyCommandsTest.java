package fr.univcotedazur.multifidelitycard.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycard.cli.context.CliClientContext;
import fr.univcotedazur.multifidelitycard.cli.context.CliContextSurvey;
import fr.univcotedazur.multifidelitycard.cli.model.CliAnswer;
import fr.univcotedazur.multifidelitycard.cli.model.CliClient;
import fr.univcotedazur.multifidelitycard.cli.model.CliQuestion;
import fr.univcotedazur.multifidelitycard.cli.model.CliSurvey;
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

import java.util.*;

import static org.mockito.Mockito.when;

@RestClientTest(SurveyCommands.class)
public class SurveyCommandsTest {
    @Autowired
    private SurveyCommands surveyCommands;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockRestServiceServer mockServer;

    @MockBean
    private CliContextSurvey cliContext;
    @MockBean
    private CliClientContext cliContextClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockServer.reset();
    }

    @Test
    public void testAddSurvey() throws Exception {
        String name = "Test Survey";
        String questions = "[What is your favorite color?]";
        CliSurvey expectedSurvey = new CliSurvey(name, Arrays.asList(new CliQuestion("What is your favorite color?")));
        String expectedResponseBody = objectMapper.writeValueAsString(expectedSurvey);

        mockServer.expect(MockRestRequestMatchers.requestTo(SurveyCommands.BASE_URI))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(expectedSurvey)))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        CliSurvey actualSurvey = surveyCommands.addSurvey(name, questions);

        Assertions.assertEquals(expectedSurvey, actualSurvey);
        mockServer.verify();

    }

    @Test
    public void testDeleteSurvey() {
        // Set up test data
        String surveyName = "Test Survey";
        CliSurvey survey = new CliSurvey(surveyName, List.of(new CliQuestion("Question 1"), new CliQuestion("Question 2")));

        Map<String, CliSurvey> map = new HashMap<>();
        map.put(surveyName, survey);

        when(cliContext.getSurveyMap()).thenReturn(map);

        // Set up mock server response
        mockServer.expect(MockRestRequestMatchers.requestTo(SurveyCommands.BASE_URI + "/" + survey.getId()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withSuccess());

        // Call method under test
        String result = surveyCommands.delete(surveyName);

        // Assert expected results
        Assertions.assertEquals("Survey deleted", result);
        Assertions.assertNull(cliContext.getSurveyMap().get(surveyName));

        // Verify mock server expectations
        mockServer.verify();
    }
}
