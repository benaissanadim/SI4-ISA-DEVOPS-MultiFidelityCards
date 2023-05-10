package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.controllers.dto.ClientDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.scheduler.ClientVFPScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
public class ClientWebValidationIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientRepository clientRepository;
    @MockBean
    private ClientVFPScheduler clientVFPScheduler;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void validCustomerTest() throws Exception {
        ClientDTO validCustomer = new ClientDTO(null, "john", "jhon@gmail.com", null);
        mockMvc.perform(MockMvcRequestBuilders.post(ClientController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("client with username john already exists");

        mockMvc.perform(MockMvcRequestBuilders.post(ClientController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void inValidCustomerNameTest() throws Exception {
        ClientDTO badCreditCardCustomer = new ClientDTO(null, "nn", "nadim.com",null);
        mockMvc.perform(MockMvcRequestBuilders.post(ClientController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badCreditCardCustomer)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void inValidCustomerEmailTest() throws Exception {
        ClientDTO badCreditCardCustomer = new ClientDTO(null, "nadim", "nadim.com",null);
        mockMvc.perform(MockMvcRequestBuilders.post(ClientController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badCreditCardCustomer)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void findAllTest() throws Exception {
        Client client1 = new Client( "John", "john@gmail.com");
        Client client2 = new Client( "Mary", "mary@gmail.com");
        
        List<Client> clients = Arrays.asList(client1, client2);
        clientRepository.saveAll(clients);

        mockMvc.perform(MockMvcRequestBuilders.get(ClientController.BASE_URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}