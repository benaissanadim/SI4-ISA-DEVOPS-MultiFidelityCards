package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.controllers.dto.CollectiveOfferDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.GiftDTO;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import fr.univcotedazur.multifidelitycards.repositories.OfferRepository;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
public class OfferWebValidationIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OfferRepository offerRepository;
    @MockBean
    private StoreRepository storeRepository;
    @MockBean
    private GeographicZoneRepository geographicZoneRepository;



    private Offer offer;


    @BeforeEach
    public void setUp() {
        offer = new Offer("test", "offer",1, OfferType.CLASSIC_OFFER);
    }

    @Test
    public void addCollectiveOffer() throws Exception {
        GeographicZone geographicZone = new GeographicZone("test");
        geographicZone.setId(1L);
        when(geographicZoneRepository.findById(1L)).thenReturn(Optional.of(geographicZone));
        CollectiveOfferDTO validOffer = new CollectiveOfferDTO(1L,"test","offer",1,geographicZone.getName(),OfferType.CLASSIC_OFFER);
        mockMvc.perform(MockMvcRequestBuilders.post(OfferController.BASE_URI +
                                        "/zone/{zoneId}", geographicZone.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOffer)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void addGift() throws Exception {
        Store store = new Store();
        store.setGifts(new ArrayList<>());
        store.setId(1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        GiftDTO validOffer = new GiftDTO(1L,"test","offer",1,store.getName(),OfferType.CLASSIC_OFFER);
        mockMvc.perform(MockMvcRequestBuilders.post(OfferController.BASE_URI + "/store/{storeId}",store.getId())
                        .contentType(MediaType.APPLICATION_JSON).
                      content(objectMapper.writeValueAsString(validOffer)))
               .andExpect(MockMvcResultMatchers.status().isCreated());
    }

        @Test
    public void testExploreAllCollectiveOffers() throws Exception {
        // Set up test data
        GeographicZone geographicZone = new GeographicZone("test");
        geographicZone.setId(1L);
        List<ParkingOffer> collectiveOffers = new ArrayList<>();
        collectiveOffers.add(new ParkingOffer("Test Collective Offer 1", "This is a test collective offer 1", 10, geographicZone,OfferType.CLASSIC_OFFER,20));
        collectiveOffers.add(new ParkingOffer("Test Collective Offer 2", "This is a test collective offer 2", 20, geographicZone,OfferType.CLASSIC_OFFER,50));
        offerRepository.saveAll(collectiveOffers);
        geographicZoneRepository.save(geographicZone);

        // Perform GET request to explore all collective offers
        mockMvc.perform(MockMvcRequestBuilders.get(OfferController.BASE_URI + "/collective"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    public void testExploreAllGifts() throws Exception {
        // Set up test data
        List<Gift> gifts = new ArrayList<>();
        Store store = new Store();
        gifts.add(new Gift("Test Gift 1", "This is a test gift 1", 5, store,OfferType.CLASSIC_OFFER));
        gifts.add(new Gift("Test Gift 2", "This is a test gift 2", 10,store, OfferType.CLASSIC_OFFER));
        offerRepository.saveAll(gifts);


        // Perform GET request to explore all gifts
        mockMvc.perform(MockMvcRequestBuilders.get(OfferController.BASE_URI + "/gift"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));


    }



}