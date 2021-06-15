package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtratoContaPoupancaService {
    @Autowired
    ExtratoContaPoupancaRepository extratoContaPoupancaRepository;

    public List<ExtratoContaPoupanca> getAllExtrato()
    {
        List<ExtratoContaPoupanca> extratoContaPoupanca = new ArrayList<ExtratoContaPoupanca>();
        extratoContaPoupancaRepository.findAll().forEach(extrato -> extratoContaPoupanca.add(extrato));
        return extratoContaPoupanca;
    }
}
