package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ContaCorrente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.model.ContaCorrenteModel;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContaCorrenteController {

        @Autowired
        ContaCorrenteService contaCorrenteService;

        @GetMapping("/contascorrentes")
        public ResponseEntity<List<ContaCorrente>> getAllContasCorrentes()
        {
            return new ResponseEntity<>(contaCorrenteService.getAllContasCorrentes(), HttpStatus.OK);
        }

        @GetMapping("/contascorrentes/{id}")
        public ResponseEntity<ContaCorrente> getContaCorrente(@PathVariable("id") long id)
        {
            try{
                var contaCorrente = contaCorrenteService.getContaCorrenteById(id);
                return new ResponseEntity<>(contaCorrente, HttpStatus.OK);
            } catch (ContaCorrenteNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }

        @DeleteMapping("/contascorrentes/{id}")
        public ResponseEntity<Integer> deleteContaCorrente(@PathVariable("id") long id)
        {
            try {
                var delete = contaCorrenteService.delete(id);
                return new ResponseEntity(delete, HttpStatus.OK);
            } catch (ContaCorrenteNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            }

        }

        @PostMapping("/contacorrente")
        public ResponseEntity<ContaCorrente> saveContaCorrente(@RequestBody ContaCorrenteModel contaCorrenteModel)
        {
            try {
                var contaCorrente = contaCorrenteService.saveOrUpdate(contaCorrenteModel);
                return new ResponseEntity<>(contaCorrente, HttpStatus.CREATED);
            } catch (ClienteNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            } catch (AgenciaNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            }

        }

        @PutMapping("/saquecontacorrente/{id}/{valor}")
        public ResponseEntity<Double> saqueContaCorrente(@PathVariable("id") long id, @PathVariable("valor") double valor)
        {
            try {
                var saque = contaCorrenteService.saqueContaCorrente(id, valor);
                return new ResponseEntity(saque, HttpStatus.OK);
            } catch (ContaCorrenteNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }

        @PutMapping("/depositocontacorrente/{id}/{valor}")
        public ResponseEntity<Double> depositoContaCorrente(@PathVariable("id") long id, @PathVariable("valor") double valor)
        {
            try {
                var deposito = contaCorrenteService.depositoContaCorrente(id, valor);
                return new ResponseEntity(deposito, HttpStatus.OK);
            } catch (ContaCorrenteNotFoundException e){
                return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }

    @PutMapping("/transferenciaentrecontascorrentesbanco/{idCCI}/{valor}/{idCCD}")
    public ResponseEntity<Double> transferenciaEntreContasCorrentesBanco(@PathVariable("idCCI") long idCCI, @PathVariable("valor") double valor, @PathVariable("idCCD") long idCCD)
    {
        try {
            var transferenciaEntreContasCorrentesBanco = contaCorrenteService.transferenciaEntreContasCorrentesBanco(idCCI, valor, idCCD);
            return new ResponseEntity(transferenciaEntreContasCorrentesBanco, HttpStatus.OK);
        } catch (ContaCorrenteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/transferenciaentrecontascioutrobanco/{idCCI}/{valor}/{idCCPExterno}")
    public ResponseEntity<Double> transferenciaEntreContasCIOutroBanco(@PathVariable("idCCI") long idCCI, @PathVariable("valor") double valor, @PathVariable("idCCPExterno") long idCCPExterno)
    {
        try {
            var transferenciaEntreContasCIOutroBanco = contaCorrenteService.transferenciaEntreContasCIOutroBanco(idCCI, valor, idCCPExterno);
            return new ResponseEntity(transferenciaEntreContasCIOutroBanco, HttpStatus.OK);
        } catch (ContaCorrenteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/transferenciacontascorrentesparacontaspoupancas/{idCCI}/{valor}/{idCPD}")
    public ResponseEntity<Double> transferenciaContasCorrentesParaContasPoupancas(@PathVariable("idCCI") long idCCI, @PathVariable("valor") double valor, @PathVariable("idCPD") long idCPD)
    {
        try {
            var transferenciaContasCorrentesParaContasPoupancas = contaCorrenteService.transferenciaContasCorrentesParaContasPoupancas(idCCI, valor, idCPD);
            return new ResponseEntity(transferenciaContasCorrentesParaContasPoupancas, HttpStatus.OK);
        } catch (ContaCorrenteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
