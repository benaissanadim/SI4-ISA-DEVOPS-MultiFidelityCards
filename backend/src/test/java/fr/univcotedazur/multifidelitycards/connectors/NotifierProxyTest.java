package fr.univcotedazur.multifidelitycards.connectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.NotifDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.Schedule;
import fr.univcotedazur.multifidelitycards.entities.Store;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class NotifierProxyTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotifierProxy notifierProxy;

    private Store store;
    private Schedule schedule;
    private Client client;

    @BeforeEach
    public void setup() {
        store = new Store();
        store.setName("Test Store");
        schedule = new Schedule();
        schedule.setDayOfWeek("Monday");
        schedule.setBeginTime("09:00");
        schedule.setEndTime("17:00");
        client = new Client();
        client.setUsername("Test Client");
        client.setEmail("testclient@example.com");
        notifierProxy.setRestTemplate(restTemplate);
    }

    @Test
    public void testNotifyFavouriteStore() {
        Set<Client> clientSet = new HashSet<>();
        clientSet.add(client);
        store.setClientsFavorite(clientSet);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("sent successfuly", HttpStatus.CREATED);
        Mockito.when(restTemplate.postForEntity(any(String.class), Mockito.any(NotifDTO.class), Mockito.eq(String.class)))
                .thenReturn(responseEntity);

        assertTrue(notifierProxy.notifyFavouriteStore(store, schedule));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());
    }

    @Test
    public void testNotifyVFP() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("sent successfuly", HttpStatus.CREATED);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(NotifDTO.class), Mockito.eq(String.class)))
                .thenReturn(responseEntity);

        assertTrue(notifierProxy.notifyVFP(client));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());
    }

    @Test
    public void testNotifyNotVFP() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("sent successfuly", HttpStatus.CREATED);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(NotifDTO.class), Mockito.eq(String.class)))
                .thenReturn(responseEntity);
        assertTrue(notifierProxy.notifyNotVFP(client));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());
    }

    @Test
    public void testNotifWhenSuccessful() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("sent successfuly", HttpStatus.CREATED);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(NotifDTO.class), Mockito.eq(String.class)))
                .thenReturn(responseEntity);
        Assertions.assertTrue(notifierProxy.notif("test@example.com", "Test Subject", "Test Message"));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());
    }

    @Test
    public void testNotifWhenFailed() {
        when(restTemplate.postForEntity(anyString(), any(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertFalse(notifierProxy.notif("test@example.com", "Test Subject", "Test Message"));

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());
    }

}