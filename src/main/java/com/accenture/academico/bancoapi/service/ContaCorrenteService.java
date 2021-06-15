package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.entity.ContaCorrente;
import com.accenture.academico.bancoapi.entity.ContaPoupanca;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.model.ContaCorrenteModel;
import com.accenture.academico.bancoapi.repository.ContaCorrenteRepository;
import com.accenture.academico.bancoapi.repository.ContaPoupancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int delete(long id) throws ContaCorrenteNotFoundException
    {
        // validacao de existencia de conta
        var contaCorrenteRetorno = contaCorrenteRepository.findById(id);
        if(contaCorrenteRetorno.isEmpty()){
            throw new ContaCorrenteNotFoundException("Conta Corrente não encontrada.");
        }
        contaCorrenteRepository.deleteById(id);
        return 200;
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
            var contaCorrenteId = contaCorrenteRepository.getById(id).getId();
            var agenciaContaCorrente = contaCorrenteRepository.getById(id).getAgencia();
            var numeroContaCorrente = contaCorrenteRepository.getById(id).getContaCorrenteNumero();
            var clienteContaCorrente = contaCorrenteRepository.getById(id).getCliente();

            var contaCorrente = new ContaCorrente(contaCorrenteId, agenciaContaCorrente, numeroContaCorrente, resultadoSaque, clienteContaCorrente);

            contaCorrenteRepository.save(contaCorrente);
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
            var contaCorrenteId = contaCorrenteRepository.getById(id).getId();
            var agenciaContaCorrente = contaCorrenteRepository.getById(id).getAgencia();
            var numeroContaCorrente = contaCorrenteRepository.getById(id).getContaCorrenteNumero();
            var clienteContaCorrente = contaCorrenteRepository.getById(id).getCliente();

            var contaCorrente = new ContaCorrente(contaCorrenteId, agenciaContaCorrente, numeroContaCorrente, resultadoDeposito, clienteContaCorrente);

            contaCorrenteRepository.save(contaCorrente);
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
        var contaCorrenteInicialSaldo = contaCorrenteRepository.findById(idCCI).get().getContaCorrenteSaldo();
        var contaCorrenteDestinoSaldo = contaCorrenteRepository.findById(idCCD).get().getContaCorrenteSaldo();

        // calculos para operacao
        var depositoContaCorrenteDestino = contaCorrenteDestinoSaldo + valorTransferencia;
        var saqueContaCorrenteInicial = contaCorrenteInicialSaldo - valorTransferencia;


        if (contaCorrenteInicialSaldo >= valorTransferencia) {
            // depósito na conta destino
            var contaCorrenteDId = contaCorrenteRepository.getById(idCCD).getId();
            var agenciaContaCorrenteD = contaCorrenteRepository.getById(idCCD).getAgencia();
            var numeroContaCorrenteD = contaCorrenteRepository.getById(idCCD).getContaCorrenteNumero();
            var clienteContaCorrenteD = contaCorrenteRepository.getById(idCCD).getCliente();

            var contaCorrenteD = new ContaCorrente(contaCorrenteDId, agenciaContaCorrenteD, numeroContaCorrenteD, depositoContaCorrenteDestino, clienteContaCorrenteD);

            contaCorrenteRepository.save(contaCorrenteD);

            // saque na conta inicial
            var contaCorrenteIId = contaCorrenteRepository.getById(idCCI).getId();
            var agenciaContaCorrenteI = contaCorrenteRepository.getById(idCCI).getAgencia();
            var numeroContaCorrenteI = contaCorrenteRepository.getById(idCCI).getContaCorrenteNumero();
            var clienteContaCorrenteI = contaCorrenteRepository.getById(idCCI).getCliente();

            var contaCorrenteI = new ContaCorrente(contaCorrenteIId, agenciaContaCorrenteI, numeroContaCorrenteI, saqueContaCorrenteInicial, clienteContaCorrenteI);

            contaCorrenteRepository.save(contaCorrenteI);

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
            var contaCorrenteIId = contaCorrenteRepository.getById(idCCI).getId();
            var agenciaContaCorrenteI = contaCorrenteRepository.getById(idCCI).getAgencia();
            var numeroContaCorrenteI = contaCorrenteRepository.getById(idCCI).getContaCorrenteNumero();
            var clienteContaCorrenteI = contaCorrenteRepository.getById(idCCI).getCliente();

            var contaCorrenteI = new ContaCorrente(contaCorrenteIId, agenciaContaCorrenteI, numeroContaCorrenteI, saqueContaCorrenteInicial, clienteContaCorrenteI);

            contaCorrenteRepository.save(contaCorrenteI);

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
        var contaCorrenteInicialSaldo = contaCorrenteRepository.findById(idCCI).get().getContaCorrenteSaldo();
        var contaPoupancaDestinoSaldo = contaPoupancaRepository.findById(idCPD).get().getContaPoupancaSaldo();

        // calculos para operacao
        var depositoContaPoupancaDestino = contaPoupancaDestinoSaldo + valorTransferencia;
        var saqueContaCorrenteInicial = contaCorrenteInicialSaldo - valorTransferencia;


        if (contaCorrenteInicialSaldo >= valorTransferencia) {
            // depósito na conta destino
            var contaPoupancaDId = contaPoupancaRepository.getById(idCPD).getId();
            var agenciaContaPoupancaD = contaPoupancaRepository.getById(idCPD).getAgencia();
            var numeroContaPoupancaD = contaPoupancaRepository.getById(idCPD).getContaPoupancaNumero();
            var clienteContaPoupancaD = contaPoupancaRepository.getById(idCPD).getCliente();

            var contaPoupancaD = new ContaPoupanca(contaPoupancaDId, agenciaContaPoupancaD, numeroContaPoupancaD, depositoContaPoupancaDestino, clienteContaPoupancaD);

            contaPoupancaRepository.save(contaPoupancaD);

            // saque na conta inicial
            var contaCorrenteIId = contaCorrenteRepository.getById(idCCI).getId();
            var agenciaContaCorrenteI = contaCorrenteRepository.getById(idCCI).getAgencia();
            var numeroContaCorrenteI = contaCorrenteRepository.getById(idCCI).getContaCorrenteNumero();
            var clienteContaCorrenteI = contaCorrenteRepository.getById(idCCI).getCliente();

            var contaCorrenteI = new ContaCorrente(contaCorrenteIId, agenciaContaCorrenteI, numeroContaCorrenteI, saqueContaCorrenteInicial, clienteContaCorrenteI);

            contaCorrenteRepository.save(contaCorrenteI);

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }
}
