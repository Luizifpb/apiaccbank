package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ExtratoContaPoupanca> getAllExtratoPorContaPoupanca(long id) throws ContaPoupancaNotFoundException {
        var extratoContaPoupancaId = getAllExtrato()
                .stream()
                .filter(extrato -> extrato.getContaPoupanca().getCliente().getId() == id)
                .collect(Collectors.toList());

        if (extratoContaPoupancaId.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Extrato vazio.");
        }
        return extratoContaPoupancaId;
    }
}
