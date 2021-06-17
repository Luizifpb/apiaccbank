package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.ContaCorrente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.repository.ContaCorrenteRepository;
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
public class ContaCorrenteRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Test
    public void testInsertAndDelete() throws Exception {

        assertThat(contaCorrenteRepository).isNotNull();

        ContaCorrente cc = new ContaCorrente();
        cc.setContaCorrenteNumero("5776");
        cc.setContaCorrenteSaldo(2000.0);
        contaCorrenteRepository.saveAndFlush(cc);

        assertThat(cc.getId()).isNotNull();

        ContaCorrente cc2 = contaCorrenteRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(cc.getContaCorrenteNumero()).isEqualTo(cc2.getContaCorrenteNumero());

        assertThat(contaCorrenteRepository.findAll()).hasSize(1);

        contaCorrenteRepository.delete(cc);

        assertThat(contaCorrenteRepository.findAll()).hasSize(0);
    }

    @Test
    public void testUpdateContaCorrente() {
        ContaCorrente testeUpdate;

        ContaCorrente cc = new ContaCorrente();
        cc.setContaCorrenteNumero("5776");
        cc.setContaCorrenteSaldo(2000.0);
        contaCorrenteRepository.saveAndFlush(cc);

        cc.setContaCorrenteNumero("66666");
        cc.setContaCorrenteSaldo(4000.0);
        contaCorrenteRepository.saveAndFlush(cc);

        testeUpdate = contaCorrenteRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testeUpdate.getContaCorrenteNumero()).isEqualTo("66666");
        assertThat(testeUpdate.getContaCorrenteSaldo()).isEqualTo(4000.0);

    }

    @Test
    public void testReadContaCorrente() {
        ContaCorrente cc = new ContaCorrente();
        cc.setContaCorrenteNumero("5776");
        cc.setContaCorrenteSaldo(2000.0);
        contaCorrenteRepository.saveAndFlush(cc);

        assertThat(contaCorrenteRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new).getContaCorrenteSaldo()).isEqualTo(2000.0);
        assertThat(contaCorrenteRepository.findById(cc.getId()).orElseThrow(NoSuchElementException::new).getContaCorrenteNumero()).isEqualTo("5776");
    }

    @Test
    public void testLançarExcessaoAgenciaNull() {

        thrown.expect(AgenciaNotFoundException.class);
        thrown.expectMessage("Agencia nao pode ser encontrada");

        ContaCorrente cc = new ContaCorrente();
        cc.setContaCorrenteNumero("5776");
        cc.setContaCorrenteSaldo(2000.0);
        contaCorrenteRepository.saveAndFlush(cc);
        if (cc.getAgencia() == null) {
            throw new AgenciaNotFoundException("Agencia nao pode ser encontrada");
        }

    }

    @Test
    public void lancarExcessaoClienteNull() {
        thrown.expect(ClienteNotFoundException.class);
        thrown.expectMessage("Cliente nao pode ser encontrado");

        ContaCorrente cc = new ContaCorrente();
        cc.setContaCorrenteNumero("5776");
        cc.setContaCorrenteSaldo(2000.0);
        contaCorrenteRepository.saveAndFlush(cc);

        if (cc.getCliente() == null) {
            throw new ClienteNotFoundException("Cliente nao pode ser encontrado");
        }
    }
}
