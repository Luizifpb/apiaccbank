package com.accenture.academico.bancoapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Agencia agencia;

    @Column(unique = true, nullable = false)
    private String contaCorrenteNumero;

    @Column(nullable = false)
    private double contaCorrenteSaldo;

    @OneToOne
    private Cliente cliente;

}
