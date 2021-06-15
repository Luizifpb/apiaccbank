package com.accenture.academico.bancoapi.repository;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtratoContaPoupancaRepository extends JpaRepository<ExtratoContaPoupanca, Long> {
}
