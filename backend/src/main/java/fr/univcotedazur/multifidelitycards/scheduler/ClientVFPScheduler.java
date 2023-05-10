package fr.univcotedazur.multifidelitycards.scheduler;

import fr.univcotedazur.multifidelitycards.components.ClientVFPService;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ClientVFPScheduler {

    Logger log = LoggerFactory.getLogger(ClientVFPScheduler.class);

    private final ClientRepository clientRepository;
    private final ClientVFPService clientVFPService;

    @Autowired
    public ClientVFPScheduler(ClientRepository clientRepository, ClientVFPService clientVFPService) {
            this.clientRepository = clientRepository;
            this.clientVFPService = clientVFPService;
        }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void updateClientsStatus() {
        log.info("ClientVFPScheduler : updateClientStatus - START");
        List<Client> clients= StreamSupport.stream(clientRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
        for (Client client : clients) {
            clientVFPService.changeStatus(client);
            clientRepository.save(client);
        }
        log.info("ClientVFPScheduler : updateClientStatus - END");
    }
}
