package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.components.OfferEligibility;
import fr.univcotedazur.multifidelitycards.components.StoreService;
import fr.univcotedazur.multifidelitycards.connectors.BankProxy;
import fr.univcotedazur.multifidelitycards.connectors.ParkingProxy;
import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.PaymentDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.ErrorDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.FidelityCardDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.PurchaseDTO;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.repositories.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
@Transactional
public class FidelityCardWebValidationIT {

    @Autowired private MockMvc mockMvc;
    @MockBean private ClientRepository clientRepository;
    @MockBean private GeographicZoneRepository geographicZoneRepository;
    @MockBean private OfferRepository offerRepository;
    @MockBean private FidelityCardRepository fidelityCardRepository;
    @MockBean private StoreService storeService;
    @MockBean private OfferEligibility offerEligibility;

    @Autowired private ObjectMapper objectMapper;
    @MockBean private BankProxy bank;
    @MockBean private ParkingProxy parkingProxy;


    private Client client;
    private GeographicZone zone;
    private FidelityCard card;
    private Store store;
    private Gift gift;
    private ParkingOffer parkingOffer;

    public FidelityCardWebValidationIT() {
    }

    @BeforeEach void setUp() {
        geographicZoneRepository.deleteAll();
        clientRepository.deleteAll();
        fidelityCardRepository.deleteAll();
        offerRepository.deleteAll();
        this.store = new Store();
        store.setName("store");
        store.setId(10L);
        this.client = new Client("john", "john@gmail.com");
        client.setId(1L);
        this.zone = new GeographicZone("antibes");
        this.card = new FidelityCard(this.client, this.zone);
        this.card.setAmount(50);
        this.card.setPoints(100);
        this.gift = new Gift();
        this.gift.setId(1L);
        this.gift.setPoints(10);
        this.gift.setStore(this.store);
        this.gift.setName("gift");
        this.parkingOffer = new ParkingOffer();
        this.parkingOffer.setId(1L);
        this.parkingOffer.setPoints(10);
        Mockito.when(this.clientRepository.findById(1L)).thenReturn(Optional.of(this.client));
        Mockito.when(this.geographicZoneRepository.findByName("antibes")).thenReturn(Optional.of(this.zone));
    }

    @Test
    public void createClientFidelityCardTest() throws Exception {

        FidelityCardDTO fidelityCard = new FidelityCardDTO(1L, "123456", 10.0, 100, "antibes", "john");
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(FidelityCardController.CLIENT_CARD_URI , 1L).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(fidelityCard)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void inValidFidelityCardTest2() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("geographic zone not found");
        errorDTO.setDetails("Geographic zone nice does not exists");
        FidelityCardDTO invalidCard = new FidelityCardDTO(1L, "123456", 10.0, 100, "nice", "john");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/clients/{clientId}/fidelityCard", 1L)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(invalidCard)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));

    }

    @Test
    public void invalidFidelityCardTest4() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("already existing fidelity card");
        errorDTO.setDetails("1");
        FidelityCardDTO invalidCard = new FidelityCardDTO(1L, "123456", 10.0, 100, "antibes", "john");
        when(this.fidelityCardRepository.findFidelityCardByClient_Id(1L)).thenReturn(Optional.of(this.card));
        when(this.fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(this.clientRepository.findById(1L)).thenReturn(Optional.of(this.client));
        when(this.geographicZoneRepository.findByName("antibes")).thenReturn(Optional.of(this.zone));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/clients/{clientId}/fidelityCard", 1L)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(invalidCard)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    public void inValidFidelityCardTest3() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("client not found");
        errorDTO.setDetails("client with id 2 not found");
        FidelityCardDTO invalidCard = new FidelityCardDTO(1L, "123456", 10.0, 100, "antibes", "john");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/clients/{clientId}/fidelityCard", 2L)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(invalidCard)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));
    }



    @Test public void buyProductTestOK() throws Exception {
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(this.card));
        when(fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(storeService.findByName(any(String.class))).thenReturn(store);
        PurchaseDTO purchaseDTO = new PurchaseDTO("MisterPizza", 50);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test public void buyProductTestKO() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("fidelity card with id 1 not found");
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(storeService.findByName(any(String.class))).thenReturn(store);
        PurchaseDTO purchaseDTO = new PurchaseDTO("MisterPizza", 50);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));


    }

    @Test public void buyProductTestKOAmount() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("not enough money to buy the product");
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(this.card));
        when(fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(storeService.findByName(any(String.class))).thenReturn(store);
        PurchaseDTO purchaseDTO = new PurchaseDTO("MisterPizza", 500);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));
    }

    @Test public void buyProductTestKOStore() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("store not found");
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(this.card));
        when(fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(storeService.findByName(any(String.class))).thenThrow(StoreNotFoundException.class);
        PurchaseDTO purchaseDTO = new PurchaseDTO("MisterPizza", 20);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));
    }

    @Test public void buyProductTestKOValid() throws Exception {
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(this.card));
        when(fidelityCardRepository.save(any(FidelityCard.class))).thenReturn(this.card);
        when(storeService.findByName(any(String.class))).thenReturn(store);
        PurchaseDTO purchaseDTO = new PurchaseDTO("MisterPizza", -20);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void rechargeWithFidelityCardValide() throws Exception {
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(bank.pay("1230896983",10.0)).thenReturn(true);
        PaymentDTO dto = new PaymentDTO("1230896983", 10.0);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/{fidelityCardId}/recharge", 1L)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void rechargeWithFidelityCardInvalid() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Invalid payment when calling bank");
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(bank.pay("1230896983",10.0)).thenReturn(false);
        PaymentDTO dto = new PaymentDTO("1230896983", 10.0);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/fidelityCards/{fidelityCardId}/recharge", 1L)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));

    }

    @Test public void receiveGiftTestOK() throws Exception {
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(offerRepository.findById(any(Long.class))).thenReturn(Optional.of(gift));
        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class), Mockito.any(Offer.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/fidelityCards/1/gift/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test public void receiveGiftTestKO() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setDetails("not enough points to retrieve");
        errorDTO.setError("not enough points exception");

        this.card.setPoints(0);

        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(offerRepository.findById(any(Long.class))).thenReturn(Optional.of(gift));
        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class),
                Mockito.any(Offer.class));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/fidelityCards/1/gift/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test public void useParkingTestOK() throws Exception {

        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class),
                Mockito.any(Offer.class));
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(offerRepository.findById(any(Long.class))).thenReturn(Optional.of(parkingOffer));
        when(parkingProxy.callParking(any(ParkingOffer.class), any(Client.class))).thenReturn(true);
        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class),
                Mockito.any(Offer.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/fidelityCards/1/parking/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test public void useParkingTestKO() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("parking proxy error");
        errorDTO.setDetails("cannot call parking server to use parking");
        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class),
                Mockito.any(Offer.class));
        when(fidelityCardRepository.findById(any(Long.class))).thenReturn(Optional.of(card));
        when(offerRepository.findById(any(Long.class))).thenReturn(Optional.of(parkingOffer));
        when(parkingProxy.callParking(any(ParkingOffer.class), any(Client.class))).thenReturn(false);
        Mockito.doNothing().when(offerEligibility).eligibleToOffer(Mockito.any(FidelityCard.class),
                Mockito.any(Offer.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/fidelityCards/1/parking/2"))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));

    }

    @Test
    public void testFindAll() throws Exception {
        Client client = new Client("john", "doe");

        GeographicZone geographicZone = new GeographicZone("antibes");
        FidelityCard fidelityCardDTO = new FidelityCard(client, geographicZone);
        List<FidelityCard> expectedList = Arrays.asList(fidelityCardDTO);
        fidelityCardRepository.saveAll(expectedList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/fidelityCards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}