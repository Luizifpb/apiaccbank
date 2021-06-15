package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.service.ExtratoContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExtratoContaCorrenteController {
    @Autowired
    ExtratoContaCorrenteService contaCorrenteService;

    @GetMapping("/extratocontacorrente")
    public ResponseEntity<List<ExtratoContaCorrente>> getAllExtrato()
    {
        return new ResponseEntity<>(contaCorrenteService.getAllExtrato(), HttpStatus.OK);
    }
}
