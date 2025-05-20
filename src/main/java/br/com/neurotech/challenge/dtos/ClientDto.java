package br.com.neurotech.challenge.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientDto {
    private Long id;
    private String name;
    private Integer age;
    private Double income;

}
