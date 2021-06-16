package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.CampoObrigatorioEmptyException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.model.ClienteModel;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClienteController {
    @Autowired
    ClienteService clienteService;

    //creating a get mapping that retrieves all the clients detail from the database
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAllCliente()
    {
        return new ResponseEntity<>(clienteService.getAllCliente(), HttpStatus.OK);
    }

    //creating a get mapping that retrieves the detail of a specific client
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable("id") long id)
    {
        try{
            var cliente = clienteService.getClienteById(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (ClienteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    //creating a delete mapping that deletes a specific client
    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<Integer> deleteCliente(@PathVariable("id") long id)
    {
        try {
            clienteService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ClienteNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

    //creating post mapping that post the client detail in the database
    @PostMapping("/cliente")
    public ResponseEntity<Cliente> saveCliente(@RequestBody ClienteModel clienteModel)
    {
        try {
            var cliente = clienteService.saveOrUpdate(clienteModel);
            return new ResponseEntity<>(cliente, HttpStatus.CREATED);
        } catch (AgenciaNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CampoObrigatorioEmptyException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity(new ErrorModel("Campo Obrigatório Inválido"), HttpStatus.NOT_FOUND);
        }

    }
}
