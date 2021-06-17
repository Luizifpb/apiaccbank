package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtratoContaCorrenteService {
    @Autowired
    ExtratoContaCorrenteRepository extratoContaCorrenteRepository;

    public List<ExtratoContaCorrente> getAllExtrato() {
        List<ExtratoContaCorrente> extratoContaCorrente = new ArrayList<ExtratoContaCorrente>();
        extratoContaCorrenteRepository.findAll().forEach(extrato -> extratoContaCorrente.add(extrato));
        return extratoContaCorrente;
    }

    public List<ExtratoContaCorrente> getAllExtratoPorContaCorrente(long id) throws ContaCorrenteNotFoundException {
        var extratoContaCorrenteId = getAllExtrato()
                .stream()
                .filter(extrato -> extrato.getContaCorrente().getCliente().getId() == id)
                .collect(Collectors.toList());

        if (extratoContaCorrenteId.isEmpty()) {
            throw new ContaCorrenteNotFoundException("Extrato vazio.");
        }
        return extratoContaCorrenteId;
    }
}
