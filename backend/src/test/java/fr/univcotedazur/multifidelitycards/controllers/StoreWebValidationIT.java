package fr.univcotedazur.multifidelitycards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelitycards.controllers.dto.*;
import fr.univcotedazur.multifidelitycards.controllers.mapper.GeographicZoneMapper;
import fr.univcotedazur.multifidelitycards.controllers.mapper.StoreMapper;
import fr.univcotedazur.multifidelitycards.entities.*;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreManager;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import org.junit.jupiter.api.Test;
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
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack
public class StoreWebValidationIT {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StoreRepository storeRepository;
    @Autowired
    private IStoreManager storeService;
    @Autowired
    private ObjectMapper objectMapper; ;
    @Autowired
    private GeographicZoneMapper geographicZoneMapper;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;
    @Autowired
    private StoreMapper storeMapper;
    @MockBean
    private ClientRepository clientRepository;



    @Test
    public void testGetStore() throws Exception {
        Long storeID = 1L;
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule(  "10:00", "20:00","Monday"),
                new Schedule(  "10:00", "20:00","Tuesday"),
                new Schedule( "10:00", "20:00","Wednesday")),new GeographicZone("antibes"));
        store.setId(storeID);
        when(storeRepository.findById(storeID)).thenReturn(Optional.of(store));
        mockMvc.perform(MockMvcRequestBuilders.get(StoreController.BASE_URI+"/" + storeID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(store)))
                .andExpect(status().isOk());

    }
    @Test
    public void registerStoreTest() throws Exception {
        List<ScheduleDTO> schedules = List.of(new ScheduleDTO( 1L,"10:00", "20:00","Monday"),new ScheduleDTO( 2L,"10:00", "20:00","Tuesday"),new ScheduleDTO( 3L,"10:00", "20:00","Wednesday"));
        GeographicZone zone = new GeographicZone("antibes");
        geographicZoneRepository.save(zone);
        StoreDTO validStore = new StoreDTO(null,"BurgerKing","23 routes des champs",schedules,"antibes");
        mockMvc.perform(MockMvcRequestBuilders.post(StoreController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void registerStoreTestZoneNotFound() throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("geographic zone not found");
        errorDTO.setDetails("Geographic zone antibes does not exists");

        List<ScheduleDTO> schedules = List.of(new ScheduleDTO( 1L,"10:00", "20:00","Monday"),
                new ScheduleDTO( 2L,"10:00", "20:00","Tuesday"),new ScheduleDTO( 3L,"10:00", "20:00","Wednesday"));
        StoreDTO validStore = new StoreDTO(null,"BurgerKing","23 routes des champs",schedules,"antibes");
        mockMvc.perform(MockMvcRequestBuilders.post(StoreController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(errorDTO)));
    }

    @Test
    public void consultStorePerAntibesZoneTest() throws Exception {
        GeographicZone zone = new GeographicZone("antibes");
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule(  "10:00", "20:00","Monday"),
                new Schedule(  "10:00", "20:00","Tuesday"),
                new Schedule( "10:00", "20:00","Wednesday")),zone);
        geographicZoneRepository.save(zone);
        storeRepository.save(store);
        mockMvc.perform(MockMvcRequestBuilders.get(StoreController.BASE_URI + "/GeoZone/antibes")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    public void addFavoriteStoreErrorTest() throws Exception {
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule(  "10:00", "20:00","Monday"),
                new Schedule(  "10:00", "20:00","Tuesday"),
                new Schedule( "10:00", "20:00","Wednesday")),new GeographicZone("antibes"));
        store.setId(1L);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post(StoreController.BASE_URI + "/" + store.getId() + "/favourites/client/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void addFavoriteStoreTest() throws Exception {
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule(  "10:00", "20:00","Monday"),
                new Schedule(  "10:00", "20:00","Tuesday"),
                new Schedule( "10:00", "20:00","Wednesday")),new GeographicZone("antibes"));
        store.setId(1L);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        Client client = new Client("test","tes@gmail.com");
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        mockMvc.perform(MockMvcRequestBuilders.post(StoreController.BASE_URI + "/" + store.getId() + "/favourites/client/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("store added to favorites"));
    }


    @Test
    public void testGetSubscribedClients() throws Exception {
        Long storeId = 1L;
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule("10:00", "20:00","Monday"),
                new Schedule("10:00", "20:00","Tuesday"),
                new Schedule("10:00", "20:00","Wednesday")),new GeographicZone("antibes"));
        store.setId(storeId);
        Set<Client> clients = Set.of(new Client("Alice","okfeoz@gmail.com"), new Client("Bob","okfleoz@gmail.com"));
        store.setClientsFavorite(clients);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        mockMvc.perform(MockMvcRequestBuilders.get(StoreController.BASE_URI + "/{storeId}/favourites", storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void scheduleTest() throws Exception {
        String beginTime = "10:00";
        String endTime = "20:00";
        String dayOfWeek = "Monday";
        ScheduleDTO scheduleDTO = new ScheduleDTO(1L, beginTime, endTime, dayOfWeek);

        Long storeID = 1L;
        Store store = new Store("BurgerKing","23 routes des champs",new ArrayList<>(),
                new GeographicZone("antibes"));
        store.setId(storeID);
        when(storeRepository.findById(storeID)).thenReturn(Optional.of(store));

        store.getSchedule().add(new Schedule(beginTime, endTime, dayOfWeek));

        mockMvc.perform(MockMvcRequestBuilders.put(StoreController.BASE_URI + "/" + storeID + "/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeMapper.toDto(store)))) // Add request body
                .andExpect(status().isCreated());
    }
    @Test
    public void testExploreAll() throws Exception {
        // create some sample stores
        List<Store> stores = List.of(
                new Store("BurgerKing","23 routes des champs",List.of(
                        new Schedule("10:00", "20:00","Monday"),
                        new Schedule("10:00", "20:00","Tuesday"),
                        new Schedule("10:00", "20:00","Wednesday")
                ),new GeographicZone("antibes")),
                new Store("McDonalds","12 rue de la paix",List.of(
                        new Schedule("11:00", "21:00","Monday"),
                        new Schedule("11:00", "21:00","Tuesday"),
                        new Schedule("11:00", "21:00","Wednesday")
                ),new GeographicZone("nice")),
                new Store("KFC","6 avenue des champs-élysées",List.of(
                        new Schedule("09:00", "22:00","Monday"),
                        new Schedule("09:00", "22:00","Tuesday"),
                        new Schedule("09:00", "22:00","Wednesday")
                ),new GeographicZone("cannes"))
        );
        // mock the storeService to return the sample stores
        storeRepository.saveAll(stores);
        // perform the request to the exploreAll endpoint
        mockMvc.perform(MockMvcRequestBuilders.get(StoreController.BASE_URI )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteTest() throws Exception {
        Store store = new Store("BurgerKing","23 routes des champs",List.of(new Schedule("10:00", "20:00","Monday"),
                new Schedule("10:00", "20:00","Tuesday"),
                new Schedule("10:00", "20:00","Wednesday")),new GeographicZone("antibes"));
        store.setId(1L);
        storeRepository.save(store);
        mockMvc.perform(MockMvcRequestBuilders.delete(StoreController.BASE_URI + "/"+ store.getId() )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}
