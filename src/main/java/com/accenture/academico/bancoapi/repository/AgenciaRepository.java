package com.accenture.academico.bancoapi.repository;

import com.accenture.academico.bancoapi.entity.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {

}
