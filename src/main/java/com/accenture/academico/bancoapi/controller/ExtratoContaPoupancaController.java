package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.model.ErrorModel;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
import com.accenture.academico.bancoapi.service.ExtratoContaPoupancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExtratoContaPoupancaController {
    @Autowired
    ExtratoContaPoupancaService extratoContaPoupancaService;
    @Autowired
    private ExtratoContaPoupancaRepository listarextratocontapoupanca;

    @GetMapping("/extratocontapoupanca")
    public ResponseEntity<List<ExtratoContaPoupanca>> getAllExtrato()
    {
        return new ResponseEntity<>(extratoContaPoupancaService.getAllExtrato(), HttpStatus.OK);
    }

    @GetMapping("/listarextratocontapoupanca/{id}")
    public ModelAndView listar(@PathVariable("id") long id) {

        List<ExtratoContaPoupanca> lista = extratoContaPoupancaService.getAllExtratoPorContaPoupanca(id);

        ModelAndView modelAndView = new ModelAndView("listarextratocontapoupanca");
        modelAndView.addObject("listarextratocontapoupanca", lista);

        return modelAndView;
    }

    @GetMapping("/extratocontapoupanca/{id}")
    public ResponseEntity<List<ExtratoContaPoupanca>> getAllExtratoPorContaPoupanca(@PathVariable("id") long id)
    {
        try{
            var extratoContaPoupanca = extratoContaPoupancaService.getAllExtratoPorContaPoupanca(id);
            return new ResponseEntity<>(extratoContaPoupanca, HttpStatus.OK);
        } catch (ContaPoupancaNotFoundException e){
            return new ResponseEntity(new ErrorModel(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
