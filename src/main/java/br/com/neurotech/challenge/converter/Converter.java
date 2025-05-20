package br.com.neurotech.challenge.converter;

import org.springframework.stereotype.Component;

@Component
public interface Converter<D,M> {
        D convertToDto(M model);

        M convertToModel(D dto);
}
