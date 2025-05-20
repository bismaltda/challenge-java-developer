package br.com.neurotech.challenge.converter;

import br.com.neurotech.challenge.dtos.ClientDto;
import br.com.neurotech.challenge.entity.NeurotechClient;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ClientConverter implements Converter<ClientDto, NeurotechClient> {

    @Override
    public ClientDto convertToDto(NeurotechClient clientModel) {
        if (clientModel == null) {
            return null;
        }
        return new ClientDto(
                clientModel.getId(),
                clientModel.getName(),
                clientModel.getAge(),
                clientModel.getIncome()
        );
    }

    @Override
    public NeurotechClient convertToModel(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        return NeurotechClient.builder()
                .name(clientDto.getName())
                .age(clientDto.getAge())
                .income(clientDto.getIncome())
                .build();
    }

}
