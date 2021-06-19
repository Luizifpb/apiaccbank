package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.ExtratoContaPoupancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExtratoContaPoupancaController {
    @Autowired
    ExtratoContaPoupancaService extratoContaPoupancaService;

    @GetMapping("/extratocontapoupanca")
    public ResponseEntity<List<ExtratoContaPoupanca>> getAllExtrato() {
        return new ResponseEntity<>(extratoContaPoupancaService.getAllExtrato(), HttpStatus.OK);
    }

    @GetMapping("/extratocontapoupanca/{id}")
    public ResponseEntity<List<ExtratoContaPoupanca>> getAllExtratoPorContaPoupanca(@PathVariable("id") long id) {
        try {
            var extratoContaPoupanca = extratoContaPoupancaService.getAllExtratoPorCliente(id);
            return new ResponseEntity<>(extratoContaPoupanca, HttpStatus.OK);
        } catch (ContaPoupancaNotFoundException e) {
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}