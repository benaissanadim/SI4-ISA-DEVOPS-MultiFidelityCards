package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.ClientDTO;
import fr.univcotedazur.multifidelitycards.entities.Client;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    public ClientDTO toDto(Client client) {
        return new ClientDTO(client.getId(), client.getUsername(), client.getEmail(),
                client.getStatus());
    }

    public Set<ClientDTO> toDtos(Set<Client> clients){
        return clients.stream().map(this::toDto).collect(Collectors.toSet());

    }

    public List<ClientDTO> toDtos(List<Client> clients){
        return clients.stream().map(this::toDto).collect(Collectors.toList());
    }

}
