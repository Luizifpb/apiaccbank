package com.accenture.academico.bancoapi.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nomeCliente;

    @CPF
    @Column(unique = true, nullable = false)
    private String cpfCliente;

    @Size(min = 16)
    @Column(unique = true, nullable = false)
    private String foneCliente;

    @ManyToOne
    @JoinColumn(name = "agencia_id")
    private Agencia agencia;

}
