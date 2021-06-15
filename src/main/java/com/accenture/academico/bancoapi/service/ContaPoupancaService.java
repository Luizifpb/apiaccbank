package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.entity.ContaCorrente;
import com.accenture.academico.bancoapi.entity.ContaPoupanca;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.model.ContaPoupancaModel;
import com.accenture.academico.bancoapi.repository.ContaCorrenteRepository;
import com.accenture.academico.bancoapi.repository.ContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContaPoupancaService {
    @Autowired
    ContaPoupancaRepository contaPoupancaRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    AgenciaService agenciaService;
    @Autowired
    ContaCorrenteRepository contaCorrenteRepository;

    public List<ContaPoupanca> getAllContasPoupancas()
    {
        List<ContaPoupanca> contasPoupancas = new ArrayList<ContaPoupanca>();
        contaPoupancaRepository.findAll().forEach(contaPoupanca -> contasPoupancas.add(contaPoupanca));
        return contasPoupancas;
    }

    public ContaPoupanca getContaPoupancaById(long id) throws ContaPoupancaNotFoundException
    {
        var contaPoupancaRetorno = contaPoupancaRepository.findById(id);
        if(contaPoupancaRetorno.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }
        return contaPoupancaRetorno.get();
    }

    public ContaPoupanca saveOrUpdate(ContaPoupancaModel contaPoupancaModel) throws AgenciaNotFoundException
    {
        var clienteRetorno = clienteService.getClienteById(contaPoupancaModel.getClienteModelId().getId());
        var agenciaRetorno = agenciaService.getAgenciaById(contaPoupancaModel.getAgenciaModelId().getId());

        var cliente = new Cliente(contaPoupancaModel.getClienteModelId().getId(), null, null, null, null);
        var agencia = new Agencia(contaPoupancaModel.getAgenciaModelId().getId(), null, null, null);
        var contaPoupanca = new ContaPoupanca(null, agencia, contaPoupancaModel.getContaPoupancaNumero(), contaPoupancaModel.getContaPoupancaSaldo(), cliente);

        var contaPoupancaRetorno = contaPoupancaRepository.save(contaPoupanca);

        contaPoupancaRetorno.setAgencia(agenciaRetorno);
        contaPoupancaRetorno.setCliente(clienteRetorno);
        return contaPoupancaRetorno;
    }

    public int delete(long id) throws ContaPoupancaNotFoundException
    {
        var contaPoupancaRetorno = contaPoupancaRepository.findById(id);
        if(contaPoupancaRetorno.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }
        contaPoupancaRepository.deleteById(id);
        return 200;
    }

    public String saqueContaPoupanca(long id, double valorSaque) throws ContaPoupancaNotFoundException
    {

        // validacao de existencia de conta
        var contaPoupancaOptional = contaPoupancaRepository.findById(id);
        if(contaPoupancaOptional.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaPoupancaSaldo = contaPoupancaRepository.findById(id).get().getContaPoupancaSaldo();
        var resultadoSaque = contaPoupancaSaldo - valorSaque;

        if (contaPoupancaSaldo >= valorSaque) {
            // saque na conta destino
            var contaPoupancaId = contaPoupancaRepository.getById(id).getId();
            var agenciaContaPoupanca = contaPoupancaRepository.getById(id).getAgencia();
            var numeroContaPoupanca = contaPoupancaRepository.getById(id).getContaPoupancaNumero();
            var clienteContaPoupanca = contaPoupancaRepository.getById(id).getCliente();

            var contaPoupanca = new ContaPoupanca(contaPoupancaId, agenciaContaPoupanca, numeroContaPoupanca, resultadoSaque, clienteContaPoupanca);

            contaPoupancaRepository.save(contaPoupanca);
            return "Saque efetuado";
        } else {
            return "Saldo insuficiente";
        }
    }

    public String depositoContaPoupanca(long id, double valorDeposito) throws ContaPoupancaNotFoundException
    {

        // validacao de existencia de conta
        var contaPoupancaOptional = contaPoupancaRepository.findById(id);
        if(contaPoupancaOptional.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaPoupancaSaldo = contaPoupancaRepository.findById(id).get().getContaPoupancaSaldo();
        var resultadoDeposito = contaPoupancaSaldo + valorDeposito;

        if (valorDeposito > 0) {
            // depósito na conta
            var contaPoupancaId = contaPoupancaRepository.getById(id).getId();
            var agenciaContaPoupanca = contaPoupancaRepository.getById(id).getAgencia();
            var numeroContaPoupanca = contaPoupancaRepository.getById(id).getContaPoupancaNumero();
            var clienteContaPoupanca = contaPoupancaRepository.getById(id).getCliente();

            var contaPoupanca = new ContaPoupanca(contaPoupancaId, agenciaContaPoupanca, numeroContaPoupanca, resultadoDeposito, clienteContaPoupanca);

            contaPoupancaRepository.save(contaPoupanca);
            return "Depósito efetuado";
        } else {
            return "Valor inválido para depósito";
        }
    }

    public String transferenciaEntreContasPoupancasBanco(long idCPI, double valorTransferencia, long idCPD) throws ContaPoupancaNotFoundException
    {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        var contaPoupancaCDOptional = contaPoupancaRepository.findById(idCPD);
        if(contaPoupancaCIOptional.isEmpty() || contaPoupancaCDOptional.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupança não encontrada.");
        }

        // pegar saldo das contas
        var contaPoupancaInicialSaldo = contaPoupancaRepository.findById(idCPI).get().getContaPoupancaSaldo();
        var contaPoupancaDestinoSaldo = contaPoupancaRepository.findById(idCPD).get().getContaPoupancaSaldo();

        // calculos para operacao
        var depositoContaPoupancaDestino = contaPoupancaDestinoSaldo + valorTransferencia;
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;


        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // depósito na conta destino
            var contaPoupancaDId = contaPoupancaRepository.getById(idCPD).getId();
            var agenciaContaPoupancaD = contaPoupancaRepository.getById(idCPD).getAgencia();
            var numeroContaPoupancaD = contaPoupancaRepository.getById(idCPD).getContaPoupancaNumero();
            var clienteContaPoupancaD = contaPoupancaRepository.getById(idCPD).getCliente();

            var contaPoupancaD = new ContaPoupanca(contaPoupancaDId, agenciaContaPoupancaD, numeroContaPoupancaD, depositoContaPoupancaDestino, clienteContaPoupancaD);

            contaPoupancaRepository.save(contaPoupancaD);

            // saque na conta inicial
            var contaPoupancaIId = contaPoupancaRepository.getById(idCPI).getId();
            var agenciaContaPoupancaI = contaPoupancaRepository.getById(idCPI).getAgencia();
            var numeroContaPoupancaI = contaPoupancaRepository.getById(idCPI).getContaPoupancaNumero();
            var clienteContaPoupancaI = contaPoupancaRepository.getById(idCPI).getCliente();

            var contaPoupancaI = new ContaPoupanca(contaPoupancaIId, agenciaContaPoupancaI, numeroContaPoupancaI, saqueContaPoupancaInicial, clienteContaPoupancaI);

            contaPoupancaRepository.save(contaPoupancaI);

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaEntreContasPoupancasOutroBanco(long idCPI, double valorTransferencia, long idCPEXterno) throws ContaCorrenteNotFoundException
    {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        if(contaPoupancaCIOptional.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta Poupança não encontrada.");
        }

        // pegar saldo da conta
        var contaPoupancaInicialSaldo = contaPoupancaRepository.findById(idCPI).get().getContaPoupancaSaldo();

        // calculos para operacao
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;

        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            var contaPoupancaIId = contaPoupancaRepository.getById(idCPI).getId();
            var agenciaContaPoupancaI = contaPoupancaRepository.getById(idCPI).getAgencia();
            var numeroContaPoupancaI = contaPoupancaRepository.getById(idCPI).getContaPoupancaNumero();
            var clienteContaPoupancaI = contaPoupancaRepository.getById(idCPI).getCliente();

            var contaPoupancaI = new ContaPoupanca(contaPoupancaIId, agenciaContaPoupancaI, numeroContaPoupancaI, saqueContaPoupancaInicial, clienteContaPoupancaI);

            contaPoupancaRepository.save(contaPoupancaI);

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaContasPoupancasParaContasCorrentes(long idCPI, double valorTransferencia, long idCPD) throws ContaPoupancaNotFoundException
    {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        var contaCorrenteCDOptional = contaCorrenteRepository.findById(idCPD);
        if(contaPoupancaCIOptional.isEmpty() || contaCorrenteCDOptional.isEmpty()){
            throw new ContaPoupancaNotFoundException("Conta não encontrada.");
        }

        // pegar saldo das contas
        var contaPoupancaInicialSaldo = contaPoupancaRepository.findById(idCPI).get().getContaPoupancaSaldo();
        var contaCorrenteDestinoSaldo = contaCorrenteRepository.findById(idCPD).get().getContaCorrenteSaldo();

        // calculos para operacao
        var depositoContaCorrenteDestino = contaCorrenteDestinoSaldo + valorTransferencia;
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;


        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // depósito na conta destino
            var contaCorrenteDId = contaCorrenteRepository.getById(idCPD).getId();
            var agenciaContaCorrenteD = contaCorrenteRepository.getById(idCPD).getAgencia();
            var numeroContaCorrenteD = contaCorrenteRepository.getById(idCPD).getContaCorrenteNumero();
            var clienteContaCorrenteD = contaCorrenteRepository.getById(idCPD).getCliente();

            var contaCorrenteD = new ContaCorrente(contaCorrenteDId, agenciaContaCorrenteD, numeroContaCorrenteD, depositoContaCorrenteDestino, clienteContaCorrenteD);

            contaCorrenteRepository.save(contaCorrenteD);

            // saque na conta inicial
            var contaPoupancaIId = contaPoupancaRepository.getById(idCPI).getId();
            var agenciaContaPoupancaI = contaPoupancaRepository.getById(idCPI).getAgencia();
            var numeroContaPoupancaI = contaPoupancaRepository.getById(idCPI).getContaPoupancaNumero();
            var clienteContaPoupancaI = contaPoupancaRepository.getById(idCPI).getCliente();

            var contaPoupancaI = new ContaPoupanca(contaPoupancaIId, agenciaContaPoupancaI, numeroContaPoupancaI, saqueContaPoupancaInicial, clienteContaPoupancaI);

            contaPoupancaRepository.save(contaPoupancaI);

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }
}
