package com.accenture.academico.bancoapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtratoContaPoupanca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT-3")
    private LocalDateTime dataHoraMovimento;

    @Column(nullable = false)
    private String operacao;

    @Column(nullable = false)
    private double valorOperacao;

    @ManyToOne
    @JoinColumn(name = "conta_poupanca_id")
    private ContaPoupanca contaPoupanca;
}
