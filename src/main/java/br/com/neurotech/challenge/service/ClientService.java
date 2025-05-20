package br.com.neurotech.challenge.service;


import br.com.neurotech.challenge.converter.Converter;
import br.com.neurotech.challenge.dtos.ClientDto;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exceptions.ClientNotFoundException;
import br.com.neurotech.challenge.repository.ClientRepository;
import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientService {

    private final Converter<ClientDto, NeurotechClient> converter;

    private final ClientRepository repository;

    public ClientService(ClientRepository repository, Converter<ClientDto, NeurotechClient> converter) {
        this.converter = converter;
        this.repository = repository;
    }

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ClientService.class);

    @Transactional
    public ClientDto create(@Valid ClientDto clientDto) {
        try {
            logger.info("Um novo cliente está sendo criado.");
            NeurotechClient client = converter.convertToModel(clientDto);
            NeurotechClient savedClient = repository.save(client);
            ClientDto dto = converter.convertToDto(savedClient);
            logger.info("Cliente {} criado !", savedClient.getId());
            return dto;
        } catch (Exception ex) {
            logger.error("Erro ao criar o cliente: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar cliente no banco de dados", ex);
        }
    }

    public ClientDto findById(Long id) {
        try {
            logger.info("Buscando cliente com ID: {}", id);
            NeurotechClient client = repository.findById(id)
                    .orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));
            ClientDto dto = converter.convertToDto(client);
            logger.info("Cliente com ID {} encontrado.", id);
            return dto;
        } catch (Exception ex) {
            logger.error("Erro ao buscar cliente por ID {}: {}", id, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por ID", ex);
        }
    }

    public List<ClientDto> findAll() {
        try {
            logger.info("Buscando de todos os clientes.");
            List<NeurotechClient> clients = repository.findAll();
            logger.info("Clientes encontrados: {}", clients.size());
            return clients.stream()
                    .map(converter::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Erro ao buscar clientes: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar clientes no banco de dados", ex);
        }
    }

    @Transactional
    public ClientDto update(Long id, @Valid ClientDto clientDto) {
        try {
            logger.info("Atualizando cliente com ID: {}", id);
            NeurotechClient client = converter.convertToModel(clientDto);

            NeurotechClient existingClient = repository.findById(id)
                    .orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));

            existingClient.setName(client.getName());
            existingClient.setAge(client.getAge());
            existingClient.setIncome(client.getIncome());

            NeurotechClient updatedClient = repository.save(existingClient);
            ClientDto dto = converter.convertToDto(updatedClient);
            logger.info("Cliente com ID {} atualizado com sucesso.", id);
            return dto;
        } catch (Exception ex) {
            logger.error("Erro ao atualizar cliente com ID {}: {}", id, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar cliente no banco de dados", ex);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            logger.info("Deletando cliente com ID: {}", id);
            if (!repository.existsById(id)) {
                throw new ClientNotFoundException("Cliente com ID " + id + " não encontrado.");
            }
            repository.deleteById(id);
            logger.info("Cliente com ID {} deletado com sucesso.", id);
        } catch (Exception ex) {
            logger.error("Erro ao excluir cliente com ID {}: {}", id, ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir cliente no banco de dados", ex);
        }
    }

}
