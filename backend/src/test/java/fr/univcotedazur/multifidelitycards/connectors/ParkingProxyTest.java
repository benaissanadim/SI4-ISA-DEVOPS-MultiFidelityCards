package fr.univcotedazur.multifidelitycards.connectors;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.ParkingDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.ParkingOffer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ParkingProxyTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ParkingProxy parkingProxy;

    @Test
    public void testCallParking() {
        // Prepare the test data
        parkingProxy.setRestTemplate(restTemplate);
        ParkingOffer parkingOffer = new ParkingOffer();
        parkingOffer.setName("Test Parking");
        Client client = new Client("test","test@example.com");

        // Prepare the mock response
        ResponseEntity<String> response = new ResponseEntity<>("Created", HttpStatus.CREATED);
        when(restTemplate.postForEntity(any(String.class), any(ParkingDTO.class), eq(String.class)))
                .thenReturn(response);

        // Call the method being tested
        boolean result = parkingProxy.callParking(parkingOffer, client);

        // Verify the result
        assertTrue(result);
        verify(restTemplate).postForEntity(
                any(String.class),
                any(ParkingDTO.class),
                eq(String.class));
    }
}