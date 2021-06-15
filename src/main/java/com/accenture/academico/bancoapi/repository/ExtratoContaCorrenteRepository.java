package com.accenture.academico.bancoapi.repository;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtratoContaCorrenteRepository extends JpaRepository<ExtratoContaCorrente, Long> {
}
