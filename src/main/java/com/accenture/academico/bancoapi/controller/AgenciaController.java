package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.model.ClienteModel;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.AgenciaService;
import com.accenture.academico.bancoapi.service.ClienteService;
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

        @GetMapping("/agencia")
        public ResponseEntity<Agencia> getAgencia()
        {
                try{
                        var agencia = agenciaService.getAgenciaById(1L);
                        return new ResponseEntity(agencia, HttpStatus.OK);
                } catch (AgenciaNotFoundException e){
                        return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
                }
        }
}
