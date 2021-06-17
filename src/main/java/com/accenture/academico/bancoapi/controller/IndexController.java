package com.accenture.academico.bancoapi.controller;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import com.accenture.academico.bancoapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    ExtratoContaCorrenteService extratoContaCorrenteService;
    @Autowired
    ExtratoContaPoupancaService extratoContaPoupancaService;
    @Autowired
    private ExtratoContaCorrenteRepository listarextrato;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContaCorrenteService contaCorrenteService;
    @Autowired
    private ContaPoupancaService contaPoupancaService;

    @GetMapping("/indexcliente/{id}")
    public ModelAndView indexcliente(@PathVariable("id") long id) {

        ModelAndView modelAndView = new ModelAndView("indexcliente");
        try {
            List<ExtratoContaCorrente> lista = extratoContaCorrenteService.getAllExtratoPorContaCorrente(id);
            modelAndView.addObject("listarextratocontacorrente", lista);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            List<ExtratoContaPoupanca> lista2 = extratoContaPoupancaService.getAllExtratoPorContaPoupanca(id);
            modelAndView.addObject("listarextratocontapoupanca", lista2);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            String cliente = clienteService.getClienteById(id).getNomeCliente();
            modelAndView.addObject("nomecliente", cliente);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            double saldocontacorrente = contaCorrenteService.getSaldoContaCorrenteByIdCliente(id);
            modelAndView.addObject("saldocontacorrente", saldocontacorrente);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            double saldocontapoupanca = contaPoupancaService.getSaldoContaPoupancaByIdCliente(id);
            modelAndView.addObject("saldocontapoupanca", saldocontapoupanca);
        } catch (Exception e) {
            e.getMessage();
        }

        return modelAndView;
    }
}
