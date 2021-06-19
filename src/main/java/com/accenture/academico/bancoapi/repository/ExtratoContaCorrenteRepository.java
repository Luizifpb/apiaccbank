package com.accenture.academico.bancoapi.repository;

import com.accenture.academico.bancoapi.entity.ContaCorrente;
import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtratoContaCorrenteRepository extends JpaRepository<ExtratoContaCorrente, Long> {
    List<ExtratoContaCorrente> findByContaCorrente(ContaCorrente contaCorrente);
}
