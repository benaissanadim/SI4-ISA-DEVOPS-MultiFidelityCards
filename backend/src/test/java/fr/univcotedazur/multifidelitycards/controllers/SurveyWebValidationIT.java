package fr.univcotedazur.multifidelitycards.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.controllers.dto.AnswerDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.SurveyDTO;
import fr.univcotedazur.multifidelitycards.controllers.mapper.QuestionMapper;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyExplorator;
import fr.univcotedazur.multifidelitycards.interfaces.ISurveyManager;
import fr.univcotedazur.multifidelitycards.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@AutoConfigureMockMvc // Full stack
public class SurveyWebValidationIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SurveyRepository surveyRepository;
    @MockBean
    private ClientRepository clientRepository;
    @Autowired
    private ISurveyManager surveyService;
    @Autowired
    private ISurveyExplorator surveyExplorator;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private SurveyController surveyController;


    private Question question1;
    private Question question2;
    private List<Question> questions;


    @BeforeEach
    public void setUp() {
        question1 = new Question("how did you like the service?");
        question2 = new Question("is the parking service efficient?");
        questions = List.of(question1, question2);
        question1.setId(2L);
    }

    @Test
    public void addSurvey() throws Exception {
        SurveyDTO validSurvey = new SurveyDTO(1L,"omar",questionMapper.toDtos(questions));
        mockMvc.perform(MockMvcRequestBuilders.post(SurveyController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSurvey)))
                .andExpect(status().isCreated());
    }


    @Test
    public void testRespondToSurvey() throws Exception {
        Long surveyId = 1L;
        Long questionId = 2L;
        Long clientId = 3L;
        String answer = "Yes";
        //create a survey with id 1
        Survey survey = new Survey();
        survey.setId(surveyId);
        survey.setQuestions(questions);
        survey.setName("omar");
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        // create a client with id 3
        Client client = new Client("client1", "izheifnhizn");
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Creating the request body
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setText(answer);


        mockMvc.perform(MockMvcRequestBuilders.post(SurveyController.BASE_URI+"/" + surveyId + "/question/" + questionId + "/reply/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testRespondToSurvey_NoSurveyFound() throws Exception {
        Long surveyId = 2L;
        Long questionId = 2L;
        Long clientId = 3L;
        String answer = "Yes";
        Client client = new Client("client1", "izheifnhizn");
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        assertThrows(NoSurveyFoundException.class , ()->surveyService.respondToQuestion(surveyId,questionId,answer,clientId));
        // Mocking the service to throw NoSurveyFoundException


        // Creating the request body
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setText(answer);

        mockMvc.perform(MockMvcRequestBuilders.post(SurveyController.BASE_URI+"/" + surveyId + "/question/" + questionId + "/reply/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetSurvey() throws Exception {
        Long surveyId = 1L;
        Survey survey = surveyService.createSurvey("test",questions);
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        mockMvc.perform(MockMvcRequestBuilders.get(SurveyController.BASE_URI+"/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk());
        String responseContent = objectMapper.writeValueAsString(survey);
        SurveyDTO surveyDTO = objectMapper.readValue(responseContent, SurveyDTO.class);
        assertEquals(surveyDTO.getId(), surveyId);
    }
    @Test
    public void testViewSurveyNotFound() throws Exception {
        Long surveyId = 1L;
        assertThrows(NoSurveyFoundException.class, () -> surveyExplorator.viewSurvey(surveyId));

        mockMvc.perform(MockMvcRequestBuilders.get(SurveyController.BASE_URI+"/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleExceptions() throws Exception {
        String errorMessage = "Invalid input";
        String fieldName = "name";
        String errorMessageTemplate = "Invalid value for %s";
        String message = String.format(errorMessageTemplate, fieldName);

        FieldError fieldError = new FieldError("surveyDTO", fieldName, errorMessage);
        BindingResult bindingResult = new BeanPropertyBindingResult("surveyDTO", "surveyDTO");
        bindingResult.addError(fieldError);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(this.getClass().getDeclaredMethod("testHandleExceptions"), -1),
                bindingResult);

        ErrorDTO expectedErrorDTO = new ErrorDTO();
        expectedErrorDTO.setError("Cannot process survey information");
        expectedErrorDTO.setDetails(message);


        ErrorDTO actualErrorDTO = surveyController.handleExceptions(exception);

        assertEquals(expectedErrorDTO.getError(), actualErrorDTO.getError());
        assertNotNull( actualErrorDTO.getDetails());
    }

    @Test
    public void testDeleteSurvey() throws Exception {
        Long surveyId = 1L;
        Survey survey = surveyService.createSurvey("test",questions);
        survey.setId(surveyId);
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        mockMvc.perform(MockMvcRequestBuilders.delete(SurveyController.BASE_URI+"/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk());

    }
    @Test
    public void testDeleteSurveyNoSurvey() throws Exception {
        Long surveyId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete(SurveyController.BASE_URI+"/" + surveyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }




}
