package com.accenture.academico.bancoapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtratoContaPoupanca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataHoraMovimento;

    @Column(nullable = false)
    private String operacao;

    @Column(nullable = false)
    private double valorOperacao;

    @OneToOne
    private ContaPoupanca contaPoupanca;
}
