package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtratoContaCorrenteService {
    @Autowired
    ExtratoContaCorrenteRepository extratoContaCorrenteRepository;

    public List<ExtratoContaCorrente> getAllExtrato()
    {
        List<ExtratoContaCorrente> extratoContaCorrente = new ArrayList<ExtratoContaCorrente>();
        extratoContaCorrenteRepository.findAll().forEach(extrato -> extratoContaCorrente.add(extrato));
        return extratoContaCorrente;
    }
}
