package com.accenture.academico.bancoapi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class AgenciaModel {

    private String nomeAgencia;
    private String enderecoAgencia;
    private String foneAgencia;

}
