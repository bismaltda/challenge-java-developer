package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.dtos.CreditCheckResponseDto;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleException;
import br.com.neurotech.challenge.repository.ClientRepository;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.neurotech.challenge.entity.VehicleModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreditService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CreditService.class);

    private final ClientRepository repository;

    public CreditService(ClientRepository repository) {
        this.repository = repository;
    }

    public CreditCheckResponseDto checkCredit(Long clientId, VehicleModel model) {
        NeurotechClient client = repository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String creditType = determineCreditType(client);

        if (model == VehicleModel.HATCH && !isEligibleForHatch(client)) {
            throw new ClientNotEligibleException("Cliente não é elegível para crédito para veículo do tipo Hatch.");
        }

        if (model == VehicleModel.SUV && !isEligibleForSuv(client)) {
            throw new ClientNotEligibleException("Cliente não é elegível para crédito para veículo do tipo SUV.");
        }

        CreditCheckResponseDto response = new CreditCheckResponseDto(
                client.getId(),
                client.getName(),
                model.name(),
                true,
                "Apto para crédito automotivo na modalidade: " + creditType
        );

        return response;
    }

    public List<Map<String, Object>> findEligibleClientsForHatch() {
        List<NeurotechClient> clients = repository.findAll();


        return clients.stream()
                .filter(client -> client.getAge() >= 23 && client.getAge() <= 49)
                .filter(client -> determineCreditType(client).contains("Fixos"))
                .filter(this::isEligibleForHatch)
                .map(client -> {
                    Map<String, Object> clientData = new HashMap<>();
                    clientData.put("name", client.getName());
                    clientData.put("income", client.getIncome());
                    return clientData;
                })
                .collect(Collectors.toList());
    }


    private String determineCreditType(NeurotechClient client) {
        int age = client.getAge();
        double income = client.getIncome();

        if (age >= 18 && age <= 25) {
            return "Crédito com Juros Fixos (5% a.a)";
        } else if (age >= 21 && age <= 65 && income >= 5000.00 && income <= 15000.00) {
            return "Crédito com Juros Variáveis";
        } else if (age > 65) {
            return "Crédito Consignado";
        } else {
            throw new ClientNotEligibleException("Cliente não atende aos critérios de crédito.");
        }
    }

    private boolean isEligibleForHatch(NeurotechClient client) {
        return client.getIncome() >= 5000.00 && client.getIncome() <= 15000.00;
    }

    private boolean isEligibleForSuv(NeurotechClient client) {
        return client.getIncome() > 8000.00 && client.getAge() > 20;
    }
}
