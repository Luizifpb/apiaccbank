package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.ExtratoContaCorrenteService;
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
public class ExtratoContaCorrenteController {
    @Autowired
    ExtratoContaCorrenteService extratoContaCorrenteService;

    @GetMapping("/extratocontacorrente")
    public ResponseEntity<List<ExtratoContaCorrente>> getAllExtrato()
    {
        return new ResponseEntity<>(extratoContaCorrenteService.getAllExtrato(), HttpStatus.OK);
    }

    @GetMapping("/extratocontacorrente/{id}")
    public ResponseEntity<List<ExtratoContaCorrente>> getAllExtratoPorContaCorrente(@PathVariable("id") long id)
    {
        try{
            var extratoContaCorrente = extratoContaCorrenteService.getAllExtratoPorContaCorrente(id);
            return new ResponseEntity<>(extratoContaCorrente, HttpStatus.OK);
        } catch (ContaCorrenteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(new ErrorModel("Campo Inválido"), HttpStatus.NOT_FOUND);
        }
    }
}