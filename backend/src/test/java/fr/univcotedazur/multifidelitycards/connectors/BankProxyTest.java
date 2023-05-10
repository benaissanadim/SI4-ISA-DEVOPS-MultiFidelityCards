package fr.univcotedazur.multifidelitycards.connectors;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.PaymentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class BankProxyTest {

    @Autowired
    private BankProxy bankProxy;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        bankProxy.restTemplate = restTemplate;
    }

    @Test
    void testPay_successfulTransaction() {
        // Arrange
        String creditCard = "1234567890";
        double value = 100.0;
        ResponseEntity<PaymentDTO> response = new ResponseEntity<>(HttpStatus.CREATED);
        when(restTemplate.postForEntity(eq("http://localhost:9090/cctransactions"), any(PaymentDTO.class), eq(PaymentDTO.class))).thenReturn(response);

        // Act
        boolean result = bankProxy.pay(creditCard, value);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testPay_badRequest() {
        // Arrange
        String creditCard = "1234567890";
        double value = 100.0;
        HttpClientErrorException error = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(any(String.class), any(PaymentDTO.class), eq(PaymentDTO.class))).thenThrow(error);

        // Act
        boolean result = bankProxy.pay(creditCard, value);

        // Assert
        assertFalse(result);
    }

    @Test
    void testPay_serverError() {
        // Arrange
        String creditCard = "1234567890";
        double value = 100.0;
        HttpClientErrorException error = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(any(String.class), any(PaymentDTO.class), eq(PaymentDTO.class))).thenThrow(error);

        // Act and assert
        assertThrows(HttpClientErrorException.class, () -> {
            bankProxy.pay(creditCard, value);
        });
    }
}