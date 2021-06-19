package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtratoContaCorrenteService {
    @Autowired
    ExtratoContaCorrenteRepository extratoContaCorrenteRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ContaCorrenteService contaCorrenteService;

    public List<ExtratoContaCorrente> getAllExtrato() {
        List<ExtratoContaCorrente> extratoContaCorrente = new ArrayList<ExtratoContaCorrente>();
        extratoContaCorrenteRepository.findAll().forEach(extrato -> extratoContaCorrente.add(extrato));
        return extratoContaCorrente;
    }

    public List<ExtratoContaCorrente> getAllExtratoPorCliente(long id) throws ContaCorrenteNotFoundException {
        var cliente = clienteService.getClienteById(id);
        var contaCorrente = contaCorrenteService.getContaCorrenteByCliente(cliente);
        var extratoContaCorrenteId = extratoContaCorrenteRepository.findByContaCorrente(contaCorrente);

        if (extratoContaCorrenteId.isEmpty()) {
            throw new ContaCorrenteNotFoundException("Extrato vazio.");
        }
        return extratoContaCorrenteId;
    }
}
