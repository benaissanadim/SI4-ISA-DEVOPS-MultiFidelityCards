package fr.univcotedazur.multifidelitycards.connectors;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.ParkingDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.ParkingOffer;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Setter
public class ParkingProxy {

    Logger log = LoggerFactory.getLogger(ParkingProxy.class);

    @Value("${parking.host.baseurl}")
    private String parkingHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    public boolean callParking(ParkingOffer parkingOffer, Client client) {
        try {
            ResponseEntity<String> result = restTemplate.postForEntity(
                    parkingHostandPort + "/parking", new ParkingDTO(parkingOffer.getName(),
                            parkingOffer.getMinutes(), client.getEmail()),
                    String.class);
            log.info(result.getBody());
            return (result.getStatusCode().equals(HttpStatus.CREATED));
        }
        catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                log.warn("Error while calling bank system" );
                return false;
            }
            throw errorException;
        }
    }
}
