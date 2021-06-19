package com.accenture.academico.bancoapi.repository;

import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.entity.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
    ContaCorrente findByCliente(Cliente cliente);
}
