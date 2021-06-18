package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.CampoObrigatorioEmptyException;
import com.accenture.academico.bancoapi.model.AgenciaModel;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.AgenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AgenciaController {
    @Autowired
    AgenciaService agenciaService;

    @GetMapping("/agencias")
    public ResponseEntity<List<Agencia>> getAllAgencia() {
        return new ResponseEntity<>(agenciaService.getAllAgencia(), HttpStatus.OK);
    }

    @GetMapping("/agencia/{id}")
    public ResponseEntity<Agencia> getAgenciaById(@PathVariable("id") long id) {
        try {
            var agencia = agenciaService.getAgenciaById(id);
            return new ResponseEntity(agencia, HttpStatus.OK);
        } catch (AgenciaNotFoundException e) {
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/agencia")
    public ResponseEntity<Agencia> saveAgencia(@RequestBody AgenciaModel agenciaModel) {
        try {
            var agencia = agenciaService.saveOrUpdate(agenciaModel);
            return new ResponseEntity(agencia, HttpStatus.CREATED);
        } catch (CampoObrigatorioEmptyException e) {
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(new ErrorModel("Campo Obrigatório Inválido"), HttpStatus.NOT_FOUND);
        }

    }
}
