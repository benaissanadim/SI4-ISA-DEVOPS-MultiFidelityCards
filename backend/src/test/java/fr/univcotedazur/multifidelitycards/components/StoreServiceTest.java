package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.controllers.dto.ScheduleDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.entities.Schedule;
import fr.univcotedazur.multifidelitycards.entities.Store;
import fr.univcotedazur.multifidelitycards.exceptions.GeographicZoneNotFoundException;
import fr.univcotedazur.multifidelitycards.exceptions.StoreNotFoundException;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreExplorator;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreFinder;
import fr.univcotedazur.multifidelitycards.interfaces.IStoreManager;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import fr.univcotedazur.multifidelitycards.repositories.GeographicZoneRepository;
import fr.univcotedazur.multifidelitycards.repositories.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StoreServiceTest {

    private Store store;
    private Store store1;
    private Schedule schedule;
    GeographicZone geographicZone;
    private List<Schedule> scheduleList;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GeographicZoneRepository geographicZoneRepository;

    @Autowired
    private IStoreManager storeManager;

    @Autowired
    private IStoreExplorator storeExplorator;

    @Autowired
    private IStoreFinder storeFinder;


    @BeforeEach
    void setUp() {
        storeRepository.save(new Store());
        this.storeRepository.deleteAll();
        schedule = new Schedule("08:00","18:00","MONDAY");
        scheduleList = new ArrayList<>();
        scheduleList.add(schedule);
        geographicZone = new GeographicZone("toulouse");
        geographicZoneRepository.save(geographicZone);
        store1 = new Store("shop","toulouse",new ArrayList<>(),geographicZone);
        storeRepository.save(store1);
    }

    @Test
    public void registerStoreTestZoneFound() throws GeographicZoneNotFoundException {
        GeographicZone geographicZone = new GeographicZone("antibes");
        geographicZoneRepository.save(geographicZone);
        store = this.storeManager.register("shop","antibes","antibes");
        Assertions.assertEquals(2, this.storeRepository.count());
        Assertions.assertEquals(store, this.storeRepository.findById(store.getId()).get());
    }

    @Test
    public void registerStoreTestZoneNotFound() throws GeographicZoneNotFoundException {
        assertThrows(GeographicZoneNotFoundException.class, () -> {
            store = this.storeManager.register("shop","antibes","antibes");
        });
    }

    @Test
    public void findByIdOk() throws StoreNotFoundException {
        Store s = storeFinder.findById(store1.getId());
        Assertions.assertEquals(store1, s);
    }

    @Test
    public void findByIdKo()  {
        assertThrows(StoreNotFoundException.class, () -> {
            storeFinder.findById(10L);
        });
    }

    @Test
    public void findByName() throws StoreNotFoundException{
        Store store = storeFinder.findByName("shop");
        Assertions.assertEquals(store1, store);
    }

    @Test
    public void findByNameKO(){
        assertThrows(StoreNotFoundException.class, () -> {
            storeFinder.findByName("shop1");
        });
    }

    @Test
    public void findByZoneName(){
        List<Store> stores = storeExplorator.consultStoresperZone("toulouse");
        Assertions.assertEquals(1, stores.size());
        Assertions.assertEquals(store1, stores.get(0));
    }

    @Test
    public void findByZoneNameZero(){
        List<Store> stores = storeExplorator.consultStoresperZone("tunis");
        Assertions.assertEquals(0, stores.size());
    }

    @Test
    public void addSchedulerError(){
        ScheduleDTO scheduleDTO = new ScheduleDTO(null,"MONDAY","10:00","17:00");
        assertThrows(EntityNotFoundException.class, () -> {
            storeManager.addSchedule(10L,scheduleDTO);
        });
    }

    @Test
    public void addSchedulerOK(){
        ScheduleDTO scheduleDTO = new ScheduleDTO(null,"10:00","17:00","MONDAY");
        storeManager.addSchedule(store1.getId(),scheduleDTO);
        assertEquals(1, store1.getSchedule().size());
        Schedule schedule = store1.getSchedule().get(0);
        assertEquals("10:00", schedule.getBeginTime());
        assertEquals("17:00", schedule.getEndTime());
        assertEquals("MONDAY", schedule.getDayOfWeek());

        scheduleDTO.setBeginTime("11:00");
        scheduleDTO.setEndTime("18:00");
        storeManager.addSchedule(store1.getId(),scheduleDTO);
        assertEquals(1, store1.getSchedule().size());
        schedule = store1.getSchedule().get(0);
        assertEquals("11:00", schedule.getBeginTime());
        assertEquals("18:00", schedule.getEndTime());
        assertEquals("MONDAY", schedule.getDayOfWeek());
    }

    @Test
    public void testFavouriteClientNotFound(){

        assertFalse(storeManager.addFavoriteStore(store1.getId(),
                10L));

    }

    @Test
    public void testFavouriteStoreNotFound(){
        assertFalse(storeManager.addFavoriteStore(10L,10L));

    }

    @Test
    public void testFavouriteStoreOk(){
        Client client = new Client("nadim", "nadim@gmail.com");
        clientRepository.save(client);
        storeManager.addFavoriteStore(store1.getId(),client.getId());
        assertEquals(1, store1.getClientsFavorite().size());
        Set<Client> clients = store1.getClientsFavorite();
        assertEquals(client, clients.stream().findFirst().orElse(null));
    }



    //test editStoreSchedule in StoreService
   /* @Test
    public void editStoreSchedule(){
        storeManager.editStoreSchedule(store1.getId(),"10:00","17:00","MONDAY");
        Assertions.assertEquals("10:00", store1.getSchedule().get(0).getBeginTime());
        Assertions.assertEquals("17:00", store1.getSchedule().get(0).getEndTime());

    }

    //do the same test but with TUESDAY
    @Test
    public void editStoreScheduleDAyShopClosed(){
        storeManager.editStoreSchedule(store1.getId(),"10:00","17:00","TUESDAY");
        Assertions.assertNotEquals("10:00", store1.getSchedule().get(0).getBeginTime());
        Assertions.assertNotEquals("17:00", store1.getSchedule().get(0).getEndTime());

    }

    @Test
    public void consultStoresperZoneStoreExist(){
        List<Store> stores = storeExplorator.consultStoresperZone(geographicZone.getName());
        Assertions.assertEquals(1, stores.size());
    }

    //same test but with a zone that doesn't exist
    @Test
    public void consultStoresperZoneStoreNotExist(){
        List<Store> stores = storeExplorator.consultStoresperZone("antibes");
        Assertions.assertEquals(0, stores.size());
    }


    */


}
