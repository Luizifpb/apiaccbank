package com.accenture.academico.bancoapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaPoupanca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Agencia agencia;

    @Column(unique = true, nullable = false)
    private String contaPoupancaNumero;

    @Column(nullable = false)
    private double contaPoupancaSaldo;

    @ManyToOne
    private Cliente cliente;

}
