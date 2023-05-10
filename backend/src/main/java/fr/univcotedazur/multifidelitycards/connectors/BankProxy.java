package fr.univcotedazur.multifidelitycards.connectors;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.PaymentDTO;
import fr.univcotedazur.multifidelitycards.interfaces.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class BankProxy implements Bank {

    @Value("${bank.host.baseurl}")
    private String bankHostandPort;

    public RestTemplate restTemplate = new RestTemplate();
    Logger log = LoggerFactory.getLogger(BankProxy.class);

    @Override
    public boolean pay(String creditCard, double value) {
        try {
            ResponseEntity<PaymentDTO> result = restTemplate.postForEntity(
                    bankHostandPort + "/cctransactions", new PaymentDTO(creditCard, value),
                    PaymentDTO.class
            );
            log.info("Payment of {}€ with credit card {}" , value, creditCard);
            return (result.getStatusCode().equals(HttpStatus.CREATED));
        }
        catch (HttpClientErrorException errorException) {
            log.warn("Error when calling bank: {}", errorException.getMessage());
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return false;
            }
            throw errorException;
        }
    }

}