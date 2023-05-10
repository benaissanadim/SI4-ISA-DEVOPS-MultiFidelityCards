package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.components.KPIsService;
import fr.univcotedazur.multifidelitycards.entities.KPIsCA;
import fr.univcotedazur.multifidelitycards.entities.KPIsOffers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
public class KPIsServiceWebValidationIT {

    @MockBean
    private KPIsService KPIsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void KPIsCAStoreTest() throws Exception {

        List<KPIsCA> list = new ArrayList<>();
        list.add(new KPIsCA(YearMonth.of(2023,1), 300, 0));
        list.add(new KPIsCA(YearMonth.of(2023,2), 450, 50));

        when(KPIsService.getKPIsCAEvolution(1L))
                .thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get(KPIsController.BASE_URI + "/store/1/ca/"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(list)));

    }

    @Test
    public void KPIsGiftsStoreTest() throws Exception {

        List<KPIsOffers> list = new ArrayList<>();
        list.add(new KPIsOffers(YearMonth.of(2023,1), 20, 0));
        list.add(new KPIsOffers(YearMonth.of(2023,2), 30, 50));

        when(KPIsService.getKPIsGiftsEvolution(1L))
                .thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get(KPIsController.BASE_URI + "/store/1/gifts/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(list)));

    }

    @Test
    public void KPIsCAStoreTestKO() throws Exception {

        when(KPIsService.getKPIsCAEvolution(1L)).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(KPIsController.BASE_URI + "/store/1/ca/"))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }

    @Test
    public void KPIsGiftsStoreTestKO() throws Exception {

        when(KPIsService.getKPIsGiftsEvolution(1L)).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(KPIsController.BASE_URI + "/store/1/gifts/"))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }
}
