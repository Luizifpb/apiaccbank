package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtratoContaPoupancaService {
    @Autowired
    ExtratoContaPoupancaRepository extratoContaPoupancaRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ContaPoupancaService contaPoupancaService;

    public List<ExtratoContaPoupanca> getAllExtrato() {
        List<ExtratoContaPoupanca> extratoContaPoupanca = new ArrayList<ExtratoContaPoupanca>();
        extratoContaPoupancaRepository.findAll().forEach(extrato -> extratoContaPoupanca.add(extrato));
        return extratoContaPoupanca;
    }

    public List<ExtratoContaPoupanca> getAllExtratoPorCliente(long id) throws ContaPoupancaNotFoundException {
        var cliente = clienteService.getClienteById(id);
        var contaPoupanca = contaPoupancaService.getContaPoupancaByCliente(cliente);
        var extratoContaPoupancaId = extratoContaPoupancaRepository.findByContaPoupanca(contaPoupanca);

        if (extratoContaPoupancaId.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Extrato vazio.");
        }
        return extratoContaPoupancaId;
    }
}