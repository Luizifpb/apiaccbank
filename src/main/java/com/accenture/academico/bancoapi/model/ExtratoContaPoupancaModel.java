package com.accenture.academico.bancoapi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class ExtratoContaPoupancaModel {

    private LocalDate dataHoraMovimento;
    private String operacao;
    private double valorOperacao;

    private ContaPoupancaModelId contaPoupancaModelId;
}
