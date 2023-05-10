package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.GeographicZoneDTO;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
public class GeographicZoneWebValidationIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GeographicZoneRepository geographicZoneService;

    @Test
    public void validZoneTest() throws Exception {
        GeographicZoneDTO validZone = new GeographicZoneDTO(null, "nice cote d'azure");
        this.mockMvc.perform(MockMvcRequestBuilders.post(GeographicZoneController.BASE_URI )
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validZone)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Geographic zone nice cote d'azure already exists");

        this.mockMvc.perform(MockMvcRequestBuilders.post(GeographicZoneController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validZone)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));

    }

    @Test
    public void inValidZoneTest() throws Exception {
        GeographicZoneDTO invalidZone = new GeographicZoneDTO(null, "");
        this.mockMvc.perform(MockMvcRequestBuilders.post(GeographicZoneController.BASE_URI )
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(invalidZone)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void findAllTest() throws Exception {
        GeographicZone zone1 = new GeographicZone( "Zone 1");
        GeographicZone zone2 = new GeographicZone( "Zone 2");
        List<GeographicZone> expectedZones = Arrays.asList(zone1, zone2);

        // Mock the geographicZoneFinder.findAll() method to return the expected zones
        geographicZoneService.saveAll(expectedZones);

        // Send a GET request to the / endpoint
        this.mockMvc.perform(MockMvcRequestBuilders.get(GeographicZoneController.BASE_URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }



}
