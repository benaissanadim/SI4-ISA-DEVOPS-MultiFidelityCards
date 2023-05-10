package fr.univcotedazur.multifidelitycards.connectors;

import fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto.NotifDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.Schedule;
import fr.univcotedazur.multifidelitycards.entities.Store;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Component
@Setter
public class NotifierProxy {

    Logger log = LoggerFactory.getLogger(NotifierProxy.class);

    @Value("${notif.host.baseurl}")
    private String notifHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    public boolean notif(String to, String subject , String text) {
        try {
            ResponseEntity<String> result = restTemplate.postForEntity(
                    notifHostandPort + "/notifications", new NotifDTO(to, subject, text),
                    String.class);
            log.info("Sending email to {}" , to);
            return (result.getStatusCode().equals(HttpStatus.CREATED));
        }
        catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                log.warn("Error while sending email to {}" , to);
                return false;
            }
            throw errorException;
        }
    }

    public boolean notifyFavouriteStore(Store store, Schedule schedule){
        String subject = "Multifidelity Cards - Favourite Store Notification";
        String text = "Hello, \n Your favourite store "
                + store.getName() + " changed its scheduler on day "+ schedule.getDayOfWeek()+ " : \n"
                +" \t -Opening time :"+ schedule.getBeginTime()
                + " \t -Closing time : " + schedule.getEndTime() ;
        log.info("Sending email to {} favourite clients" , store.getName());
        Set<Client> clientSet = store.getClientsFavorite();
        for (Client client : clientSet) {
            log.info(client.getEmail());
            notif(client.getEmail(), subject, text);
        }
        return true;
    }

    public boolean notifyVFP(Client client){
        String subject = "Multifidelity Cards - VFP Notification";
        String text = "Hello "+client.getUsername()+","+
                "  \n CONGRATULATIONS !! "
                + "\n This week, you became a Very Faithfull Client of Our Multifidelity Cards !"
                + " You can now benifit from more reductions and offers" ;
          return  notif(client.getEmail(), subject, text);
    }

    public boolean notifyNotVFP(Client client){
        String subject = "Multifidelity Cards - VFP Notification";
        String text = "Hello "+client.getUsername()+
                ",  \n Unfortunately, due to your lack of fidelity, you are no longer a Very Faithfull Client of Our Multifidelity Cards !"
                + "\n Try to be more loyal to our stores and you will be able to benifit from more reductions and offers" ;
      return   notif(client.getEmail(), subject, text);
    }

}