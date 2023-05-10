package fr.univcotedazur.multifidelitycards.scheduler;

import fr.univcotedazur.multifidelitycards.components.ClientVFPService;
import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ClientVFPSchedulerTest {

    @Autowired
    private ClientRepository clientRepository;
    @MockBean
    private ClientVFPService clientVFPService;
    @Autowired
    private ClientVFPScheduler clientVFPScheduler;

    @Test
    public void test(){
        Client client1 = new Client("client1", "client1");
        Client client2 = new Client("client2", "client2");
        Client client3 = new Client("client3", "client3");
        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
        clientVFPScheduler.updateClientsStatus();
        verify(clientVFPService, times(1)).changeStatus(client1);
        verify(clientVFPService, times(1)).changeStatus(client2);
        verify(clientVFPService, times(1)).changeStatus(client3);
    }


}
