package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.service.ExtratoContaPoupancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExtratoContaPoupancaController {
    @Autowired
    ExtratoContaPoupancaService contaPoupancaService;

    @GetMapping("/extratocontapoupanca")
    public ResponseEntity<List<ExtratoContaPoupanca>> getAllExtrato()
    {
        return new ResponseEntity<>(contaPoupancaService.getAllExtrato(), HttpStatus.OK);
    }
}
