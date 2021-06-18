package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.entity.ContaPoupanca;
import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.model.ContaPoupancaModel;
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
public class ContaPoupancaService {
    @Autowired
    ContaPoupancaRepository contaPoupancaRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    AgenciaService agenciaService;
    @Autowired
    ContaCorrenteRepository contaCorrenteRepository;
    @Autowired
    ExtratoContaCorrenteRepository extratoContaCorrenteRepository;
    @Autowired
    ExtratoContaPoupancaRepository extratoContaPoupancaRepository;
    @Autowired
    ContaPoupancaService contaPoupancaService;
    @Autowired
    ExtratoContaPoupancaService extratoContaPoupancaService;

    public List<ContaPoupanca> getAllContasPoupancas() {
        List<ContaPoupanca> contasPoupancas = new ArrayList<ContaPoupanca>();
        contaPoupancaRepository.findAll().forEach(contaPoupanca -> contasPoupancas.add(contaPoupanca));
        return contasPoupancas;
    }

    public ContaPoupanca getContaPoupancaById(long id) throws ContaPoupancaNotFoundException {
        var contaPoupancaRetorno = contaPoupancaRepository.findById(id);
        if (contaPoupancaRetorno.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }
        return contaPoupancaRetorno.get();
    }

    public double getSaldoContaPoupancaByIdCliente(long id) throws ContaCorrenteNotFoundException {
        // validacao de existencia de conta
        var getSaldoContaPoupancaByIdCliente = getAllContasPoupancas().stream()
                .filter(conta -> conta.getCliente().getId() == id).findFirst().get();

        var saldo = getSaldoContaPoupancaByIdCliente.getContaPoupancaSaldo();

        return saldo;
    }

    public ContaPoupanca saveOrUpdate(ContaPoupancaModel contaPoupancaModel) throws AgenciaNotFoundException {
        var clienteRetorno = clienteService.getClienteById(contaPoupancaModel.getClienteModelId().getId());
        var agenciaRetorno = agenciaService.getAgenciaById(contaPoupancaModel.getAgenciaModelId().getId());

        var cliente = new Cliente(contaPoupancaModel.getClienteModelId().getId(), null, null, null, null);
        var agencia = new Agencia(contaPoupancaModel.getAgenciaModelId().getId(), null, null, null);
        var contaPoupanca = new ContaPoupanca(null, agencia, gerarNumeroContaPoupanca(), 0, cliente);

        var contaPoupancaRetorno = contaPoupancaRepository.save(contaPoupanca);

        contaPoupancaRetorno.setAgencia(agenciaRetorno);
        contaPoupancaRetorno.setCliente(clienteRetorno);
        return contaPoupancaRetorno;
    }

    public Boolean deleteContaPoupanca(long id) throws ContaPoupancaNotFoundException {
        var contaPoupancaRetorno = contaPoupancaRepository.findById(id);
        if (contaPoupancaRetorno.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }
        contaPoupancaRepository.deleteById(id);

        return true;
    }

    public String saqueContaPoupanca(long id, double valorSaque) throws ContaPoupancaNotFoundException {
        // validacao de existencia de conta
        var contaPoupancaOptional = contaPoupancaRepository.findById(id);
        if (contaPoupancaOptional.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaPoupancaSaldo = contaPoupancaRepository.findById(id).get().getContaPoupancaSaldo();
        var resultadoSaque = contaPoupancaSaldo - valorSaque;

        if (contaPoupancaSaldo >= valorSaque) {
            // saque na conta destino
            operacaoContaPoupanca(id, resultadoSaque, valorSaque, "Saque");
            return "Saque efetuado";
        } else {
            return "Saldo insuficiente";
        }
    }

    public String depositoContaPoupanca(long id, double valorDeposito) throws ContaPoupancaNotFoundException {

        // validacao de existencia de conta
        var contaPoupancaOptional = contaPoupancaRepository.findById(id);
        if (contaPoupancaOptional.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupanca não encontrada.");
        }

        // pegar saldo da conta e calcular saque
        var contaPoupancaSaldo = contaPoupancaRepository.findById(id).get().getContaPoupancaSaldo();
        var resultadoDeposito = contaPoupancaSaldo + valorDeposito;

        if (valorDeposito > 0) {
            // depósito na conta
            operacaoContaPoupanca(id, resultadoDeposito, valorDeposito, "Depósito");

            return "Depósito efetuado";
        } else {
            return "Valor inválido para depósito";
        }
    }

    public String transferenciaEntreContasPoupancasBanco(long idCPI, double valorTransferencia, long idCPD) throws ContaPoupancaNotFoundException {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        var contaPoupancaCDOptional = contaPoupancaRepository.findById(idCPD);
        if (contaPoupancaCIOptional.isEmpty() || contaPoupancaCDOptional.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupança não encontrada.");
        }

        if (valorTransferencia <= 0) {
            throw new ContaCorrenteNotFoundException("Valor inválido.");
        }

        // pegar saldo das contas
        var contaPoupancaInicialSaldo = contaPoupancaCIOptional.get().getContaPoupancaSaldo();
        var contaPoupancaDestinoSaldo = contaPoupancaCDOptional.get().getContaPoupancaSaldo();

        // calculos para operacao
        var depositoContaPoupancaDestino = contaPoupancaDestinoSaldo + valorTransferencia;
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;


        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaPoupanca(idCPI, saqueContaPoupancaInicial, valorTransferencia, "Transferência Realizada");

            // depósito na conta destino
            operacaoContaPoupanca(idCPD, depositoContaPoupancaDestino, valorTransferencia, "Transferência Recebida");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaEntreContasPIOutroBanco(long idCPI, double valorTransferencia, long idCPCEXterno) throws ContaCorrenteNotFoundException {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        if (contaPoupancaCIOptional.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta Poupança não encontrada.");
        }

        if (valorTransferencia <= 0) {
            throw new ContaCorrenteNotFoundException("Valor inválido.");
        }

        // pegar saldo da conta
        var contaPoupancaInicialSaldo = contaPoupancaRepository.findById(idCPI).get().getContaPoupancaSaldo();

        // calculos para operacao
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;

        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaPoupanca(idCPI, saqueContaPoupancaInicial, valorTransferencia, "Transferência Realizada");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String transferenciaContasPoupancasParaContasCorrentes(long idCPI, double valorTransferencia, long idCCD) throws ContaPoupancaNotFoundException {

        // validacao de existencia de conta
        var contaPoupancaCIOptional = contaPoupancaRepository.findById(idCPI);
        var contaCorrenteCDOptional = contaCorrenteRepository.findById(idCCD);
        if (contaPoupancaCIOptional.isEmpty() || contaCorrenteCDOptional.isEmpty()) {
            throw new ContaPoupancaNotFoundException("Conta não encontrada.");
        }

        if (valorTransferencia <= 0) {
            throw new ContaCorrenteNotFoundException("Valor inválido.");
        }

        // pegar saldo das contas
        var contaPoupancaInicialSaldo = contaPoupancaCIOptional.get().getContaPoupancaSaldo();
        var contaCorrenteDestinoSaldo = contaCorrenteCDOptional.get().getContaCorrenteSaldo();

        // calculos para operacao
        var depositoContaCorrenteDestino = contaCorrenteDestinoSaldo + valorTransferencia;
        var saqueContaPoupancaInicial = contaPoupancaInicialSaldo - valorTransferencia;


        if (contaPoupancaInicialSaldo >= valorTransferencia) {
            // saque na conta inicial
            operacaoContaPoupanca(idCPI, saqueContaPoupancaInicial, valorTransferencia, "Transferência Realizada");

            // depósito na conta destino
            operacaoContaCorrente(idCCD, depositoContaCorrenteDestino, valorTransferencia, "Transferência Recebida");

            return "Transferência efetuada";
        } else {
            return "Valor inválido para transferência";
        }
    }

    public String recalcularSaldoContaPoupanca(long id) {
        var saldoAtual = contaPoupancaService.getSaldoContaPoupancaByIdCliente(id);
        var listaExtratoContaPoupanca = extratoContaPoupancaService.getAllExtratoPorContaPoupanca(id);

        double valorSaques = 0, valorDepositos = 0, valorTransferenciasRealizadas = 0, valorTransferenciasRecebidas = 0;
        double valorTotalExtrato = 0;
        for (ExtratoContaPoupanca operacao : listaExtratoContaPoupanca) {
            if (operacao.getOperacao().equals("Saque")) {
                valorSaques = valorSaques + operacao.getValorOperacao();
            }
            if (operacao.getOperacao().equals("Depósito")) {
                valorDepositos = valorDepositos + operacao.getValorOperacao();
            }
            if (operacao.getOperacao().equals("Transferência Realizada")) {
                valorTransferenciasRealizadas = valorTransferenciasRealizadas + operacao.getValorOperacao();
            }
            if (operacao.getOperacao().equals("Transferência Recebida")) {
                valorTransferenciasRecebidas = valorTransferenciasRecebidas + operacao.getValorOperacao();
            }
        }
        valorTotalExtrato = (valorDepositos + valorTransferenciasRecebidas) - (valorSaques + valorTransferenciasRealizadas);

        // buscar id da conta
        var getContaPoupancaByIdCliente = getAllContasPoupancas().stream()
                .filter(idconta -> idconta.getCliente().getId() == id).findFirst().get();
        var contaId = getContaPoupancaByIdCliente.getId();

        if (valorTotalExtrato == saldoAtual) {
            return "O saldo está correto.";
        } else {
            contaPoupancaService.getContaPoupancaById(contaId).setContaPoupancaSaldo(valorTotalExtrato);
            contaPoupancaRepository.save(getContaPoupancaByIdCliente);
            return "O seu saldo foi atualizado.";
        }
    }

    public void operacaoContaPoupanca(long id, double resultadoOperacao, double valorOperacao, String operacao) {
        var contaPoupancaId = contaPoupancaRepository.getById(id).getId();
        var agenciaContaPoupanca = contaPoupancaRepository.getById(id).getAgencia();
        var numeroContaPoupanca = contaPoupancaRepository.getById(id).getContaPoupancaNumero();
        var clienteContaPoupanca = contaPoupancaRepository.getById(id).getCliente();

        var contaPoupanca = new ContaPoupanca(contaPoupancaId, agenciaContaPoupanca, numeroContaPoupanca, resultadoOperacao, clienteContaPoupanca);

        contaPoupancaRepository.save(contaPoupanca);

        LocalDateTime data = LocalDateTime.now();
        var extratoContaPoupanca = new ExtratoContaPoupanca(null, data, operacao, valorOperacao, contaPoupanca);
        extratoContaPoupancaRepository.save(extratoContaPoupanca);
    }

    public void operacaoContaCorrente(long id, double resultadoOperacao, double valorOperacao, String operacao) {
        var contaPoupancaIId = contaPoupancaRepository.getById(id).getId();
        var agenciaContaPoupancaI = contaPoupancaRepository.getById(id).getAgencia();
        var numeroContaPoupancaI = contaPoupancaRepository.getById(id).getContaPoupancaNumero();
        var clienteContaPoupancaI = contaPoupancaRepository.getById(id).getCliente();

        var contaPoupancaI = new ContaPoupanca(contaPoupancaIId, agenciaContaPoupancaI, numeroContaPoupancaI, resultadoOperacao, clienteContaPoupancaI);

        contaPoupancaRepository.save(contaPoupancaI);

        LocalDateTime data = LocalDateTime.now();
        var extratoContaPoupanca = new ExtratoContaPoupanca(null, data, operacao, valorOperacao, contaPoupancaI);
        extratoContaPoupancaRepository.save(extratoContaPoupanca);
    }

    public String gerarNumeroContaPoupanca() {
        var size = contaPoupancaService.getAllContasPoupancas().size();
        int numero = size + 1;
        var numeroContaPoupanca = Integer.toString(numero);
        return numeroContaPoupanca;
    }
}
