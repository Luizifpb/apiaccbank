package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.ContaPoupanca;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.repository.AgenciaRepository;
import com.accenture.academico.bancoapi.repository.ClienteRepository;
import com.accenture.academico.bancoapi.repository.ContaPoupancaRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ContaPoupancaRepositoryTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository;
    @Autowired
    private AgenciaRepository agenciaRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void createAndDeleteContaPoupanca() {
        ContaPoupanca contaPoupanca = new ContaPoupanca();
        contaPoupanca.setContaPoupancaNumero("1234 1234 1234");
        contaPoupanca.setContaPoupancaSaldo(1200.0);

        this.contaPoupancaRepository.save(contaPoupanca);

        Assertions.assertThat(contaPoupanca.getId()).isNotNull();
        Assertions.assertThat(contaPoupanca.getContaPoupancaNumero()).isEqualTo("1234 1234 1234");
        Assertions.assertThat(contaPoupanca.getContaPoupancaSaldo()).isEqualTo(1200.0);

        assertThat(contaPoupancaRepository.findAll()).hasSize(1);

        contaPoupancaRepository.delete(contaPoupanca);

        thrown.expect(NoSuchElementException.class);
        contaPoupancaRepository.findById(contaPoupanca.getId()).orElseThrow(NoSuchElementException::new);

    }

    @Test
    public void testUpdateContaPoupanca() {
        ContaPoupanca testeUpdate;

        ContaPoupanca cc = new ContaPoupanca();
        cc.setContaPoupancaNumero("5776");
        cc.setContaPoupancaSaldo(2000.0);
        contaPoupancaRepository.saveAndFlush(cc);

        cc.setContaPoupancaNumero("66666");
        cc.setContaPoupancaSaldo(4000.0);
        contaPoupancaRepository.saveAndFlush(cc);

        testeUpdate = contaPoupancaRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testeUpdate.getContaPoupancaNumero()).isEqualTo("66666");
        assertThat(testeUpdate.getContaPoupancaSaldo()).isEqualTo(4000.0);

    }

    @Test
    public void testReadContaPoupanca() {
        ContaPoupanca cc = new ContaPoupanca();
        cc.setContaPoupancaNumero("5776");
        cc.setContaPoupancaSaldo(2000.0);
        contaPoupancaRepository.saveAndFlush(cc);

        assertThat(contaPoupancaRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new).getContaPoupancaSaldo()).isEqualTo(2000.0);
        assertThat(contaPoupancaRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new).getContaPoupancaNumero()).isEqualTo("5776");
    }

    @Test
    public void testLançarExcessaoAgenciaNull() {

        thrown.expect(AgenciaNotFoundException.class);
        thrown.expectMessage("Agencia nao pode ser encontrada");

        ContaPoupanca cc = new ContaPoupanca();
        cc.setContaPoupancaNumero("5776");
        cc.setContaPoupancaSaldo(2000.0);
        contaPoupancaRepository.saveAndFlush(cc);
        if (cc.getAgencia() == null) {
            throw new AgenciaNotFoundException("Agencia nao pode ser encontrada");
        }

    }

    @Test
    public void lancarExcessaoClienteNull() {
        thrown.expect(ClienteNotFoundException.class);
        thrown.expectMessage("Cliente nao pode ser encontrado");

        ContaPoupanca cc = new ContaPoupanca();
        cc.setContaPoupancaNumero("5776");
        cc.setContaPoupancaSaldo(2000.0);
        contaPoupancaRepository.saveAndFlush(cc);

        if (cc.getCliente() == null) {
            throw new ClienteNotFoundException("Cliente nao pode ser encontrado");
        }
    }

}




