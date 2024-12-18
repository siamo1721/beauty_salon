package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Client;
import com.example.Kurs_salon.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}