package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.*;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.model.ContaCorrenteModel;
import com.accenture.academico.bancoapi.repository.ContaCorrenteRepository;
import com.accenture.academico.bancoapi.repository.ContaPoupancaRepository;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContaCorrenteService {
    @Autowired
    ContaCorrenteRepository contaCorrenteRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    AgenciaService agenciaService;
    @Autowired
    ContaPoupancaRepository contaPoupancaRepository;
    @Autowired
    ExtratoContaCorrenteRepository extratoContaCorrenteRepository;
    @Autowired
    ExtratoContaPoupancaRepository extratoContaPoupancaRepository;

    public List<ContaCorrente> getAllContasCorrentes()
    {
        List<ContaCorrente> contasCorrentes = new ArrayList<ContaCorrente>();
        contaCorrenteRepository.findAll().forEach(contaCorrente -> contasCorrentes.add(contaCorrente));
        return contasCorrentes;
    }

    public ContaCorrente getContaCorrenteById(long id) throws ContaCorrenteNotFoundException
    {
        // validacao de existencia de conta
        var contaCorrenteRetorno = contaCorrenteRepository.findById(id);
        if(contaCorrenteRetorno.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }
        return contaCorrenteRetorno.get();
    }

    public ContaCorrente saveOrUpdate(ContaCorrenteModel contaCorrenteModel) throws AgenciaNotFoundException
    {
        var clienteRetorno = clienteService.getClienteById(contaCorrenteModel.getClienteModelId().getId());
        var agenciaRetorno = agenciaService.getAgenciaById(contaCorrenteModel.getAgenciaModelId().getId());

        var cliente = new Cliente(contaCorrenteModel.getClienteModelId().getId(), null, null, null, null);
        var agencia = new Agencia(contaCorrenteModel.getAgenciaModelId().getId(), null, null, null);
        var contaCorrente = new ContaCorrente(null, agencia, contaCorrenteModel.getContaCorrenteNumero(), contaCorrenteModel.getContaCorrenteSaldo(), cliente);

        var contaCorrenteRetorno = contaCorrenteRepository.save(contaCorrente);

        contaCorrenteRetorno.setAgencia(agenciaRetorno);
        contaCorrenteRetorno.setCliente(clienteRetorno);
        return contaCorrenteRetorno;
    }

    public Boolean deleteContaCorrente(long id) throws ContaCorrenteNotFoundException
    {
        // validacao de existencia de conta
        var contaCorrenteRetorno = contaCorrenteRepository.findById(id);
        if(contaCorrenteRetorno.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }
        contaCorrenteRepository.deleteById(id);

        return true;
    }

    public String saqueContaCorrente(long id, double valorSaque) throws ContaCorrenteNotFoundException
    {
        // validacao de existencia de conta
        var contaCorrenteOptional = contaCorrenteRepository.findById(id);
        if(contaCorrenteOptional.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaCorrenteSaldo = contaCorrenteRepository.findById(id).get().getContaCorrenteSaldo();
        var resultadoSaque = contaCorrenteSaldo - valorSaque;

        if (contaCorrenteSaldo >= valorSaque) {
            // saque na conta destino
            operacaoContaCorrente(id, resultadoSaque, valorSaque, "Saque");
            return "Saque efetuado";
        } else {
            return "Saldo insuficiente";
        }
    }

    public String depositoContaCorrente(long id, double valorDeposito) throws ContaCorrenteNotFoundException
    {
        // validacao de existencia de conta
        var contaCorrenteOptional = contaCorrenteRepository.findById(id);
        if(contaCorrenteOptional.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaCorrenteSaldo = contaCorrenteRepository.findById(id).get().getContaCorrenteSaldo();
        var resultadoDeposito = contaCorrenteSaldo + valorDeposito;

        if (valorDeposito > 0) {
            // depósito na conta
            operacaoContaCorrente(id, resultadoDeposito, valorDeposito, "Depósito");
            return "Depósito efetuado";
        } else {
            return "Valor inválido para depósito";
        }
    }

    public String transferenciaEntreContasCorrentesBanco(long idCCI, double valorTransferencia, long idCCD) throws ContaCorrenteNotFoundException
    {

        // validacao de existencia de conta
        var contaCorrenteCIOptional = contaCorrenteRepository.findById(idCCI);
        var contaCorrenteCDOptional = contaCorrenteRepository.findById(idCCD);
        if(contaCorrenteCIOptional.isEmpty() || contaCorrenteCDOptional.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }

        // pegar saldo das contas
        var contaCorrenteInicialSaldo = contaCorrenteCIOptional.get().getContaCorrenteSaldo();
        var contaCorrenteDestinoSaldo = contaCorrenteCDOptional.get().getContaCorrenteSaldo();

        // calculos para operacao
        var depositoContaCorrenteDestino = contaCorrenteDestinoSaldo + valorTransferencia;
        var saqueContaCorrenteInicial = contaCorrenteInicialSaldo - valorTransferencia;


        if (contaCorrenteInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaCorrente(idCCI, saqueContaCorrenteInicial, valorTransferencia, "Transferência Realizada");

            // depósito na conta destino
            operacaoContaCorrente(idCCD, depositoContaCorrenteDestino, valorTransferencia, "Transferência Recebida");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaEntreContasCIOutroBanco(long idCCI, double valorTransferencia, long idCCPEXterno) throws ContaCorrenteNotFoundException
    {

        // validacao de existencia de conta
        var contaCorrenteCIOptional = contaCorrenteRepository.findById(idCCI);
        if(contaCorrenteCIOptional.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }

        // pegar saldo da conta
        var contaCorrenteInicialSaldo = contaCorrenteRepository.findById(idCCI).get().getContaCorrenteSaldo();

        // calculos para operacao
        var saqueContaCorrenteInicial = contaCorrenteInicialSaldo - valorTransferencia;

        if (contaCorrenteInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaCorrente(idCCI, saqueContaCorrenteInicial, valorTransferencia, "Transferência Realizada");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaContasCorrentesParaContasPoupancas(long idCCI, double valorTransferencia, long idCPD) throws ContaCorrenteNotFoundException
    {

        // validacao de existencia de conta
        var contaCorrenteCIOptional = contaCorrenteRepository.findById(idCCI);
        var contaPoupancaCDOptional = contaPoupancaRepository.findById(idCPD);
        if(contaCorrenteCIOptional.isEmpty() || contaPoupancaCDOptional.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta não encontrada.");
        }

        // pegar saldo das contas
        var contaCorrenteInicialSaldo = contaCorrenteCIOptional.get().getContaCorrenteSaldo();
        var contaPoupancaDestinoSaldo = contaPoupancaCDOptional.get().getContaPoupancaSaldo();

        // calculos para operacao
        var depositoContaPoupancaDestino = contaPoupancaDestinoSaldo + valorTransferencia;
        var saqueContaCorrenteInicial = contaCorrenteInicialSaldo - valorTransferencia;


        if (contaCorrenteInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaCorrente(idCCI, saqueContaCorrenteInicial, valorTransferencia, "Transferência Realizada");

            // depósito na conta destino
            operacaoContaPoupanca(idCPD, depositoContaPoupancaDestino, valorTransferencia, "Transferência Recebida");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public void operacaoContaCorrente(long id, double resultadoOperacao, double valorOperacao, String operacao){
        var contaCorrenteId = contaCorrenteRepository.getById(id).getId();
        var agenciaContaCorrente = contaCorrenteRepository.getById(id).getAgencia();
        var numeroContaCorrente = contaCorrenteRepository.getById(id).getContaCorrenteNumero();
        var clienteContaCorrente = contaCorrenteRepository.getById(id).getCliente();

        var contaCorrente = new ContaCorrente(contaCorrenteId, agenciaContaCorrente, numeroContaCorrente, resultadoOperacao, clienteContaCorrente);

        contaCorrenteRepository.save(contaCorrente);

        LocalDateTime data = LocalDateTime.now();
        var extratoContaCorrente = new ExtratoContaCorrente(null, data, operacao, valorOperacao, contaCorrente);
        extratoContaCorrenteRepository.save(extratoContaCorrente);
    }

    public void operacaoContaPoupanca(long id, double resultadoOperacao, double valorOperacao, String operacao){
        var contaPoupancaDId = contaPoupancaRepository.getById(id).getId();
        var agenciaContaPoupancaD = contaPoupancaRepository.getById(id).getAgencia();
        var numeroContaPoupancaD = contaPoupancaRepository.getById(id).getContaPoupancaNumero();
        var clienteContaPoupancaD = contaPoupancaRepository.getById(id).getCliente();

        var contaPoupancaD = new ContaPoupanca(contaPoupancaDId, agenciaContaPoupancaD, numeroContaPoupancaD, resultadoOperacao, clienteContaPoupancaD);

        contaPoupancaRepository.save(contaPoupancaD);

        LocalDateTime data = LocalDateTime.now();
        var extratoContaPoupanca = new ExtratoContaPoupanca(null, data, operacao, valorOperacao, contaPoupancaD);
        extratoContaPoupancaRepository.save(extratoContaPoupanca);
    }

}
