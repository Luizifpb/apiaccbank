package com.accenture.academico.bancoapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nomeAgencia = "Agência Central";

    @Column(unique = true, nullable = false)
    private String enderecoAgencia = "Rua Floriano Peixoto - 101, Centro, Campina Grande - PB";

    @Size(min = 11)
    @Column(unique = true, nullable = false)
    private String foneAgencia = "0800 888 9009";

}
