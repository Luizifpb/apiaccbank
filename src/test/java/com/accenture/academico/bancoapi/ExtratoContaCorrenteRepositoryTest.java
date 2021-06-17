package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.ExtratoContaCorrente;
import com.accenture.academico.bancoapi.exception.ContaCorrenteNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaCorrenteRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ExtratoContaCorrenteRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private ExtratoContaCorrenteRepository extratoContaCorrenteRepository;

    @Test
    public void testInsertAndDelete() throws Exception {

        assertThat(extratoContaCorrenteRepository).isNotNull();

        ExtratoContaCorrente ecc = new ExtratoContaCorrente();
        ecc.setDataHoraMovimento(LocalDateTime.now());
        ecc.setOperacao("saque");
        ecc.setValorOperacao(300.0);
        extratoContaCorrenteRepository.saveAndFlush(ecc);

        assertThat(ecc.getId()).isNotNull();

        ExtratoContaCorrente ecp2 = extratoContaCorrenteRepository.findById(ecc.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(ecc.getOperacao()).isEqualTo(ecp2.getOperacao());

        assertThat(extratoContaCorrenteRepository.findAll()).hasSize(1);

        extratoContaCorrenteRepository.delete(ecc);

        assertThat(extratoContaCorrenteRepository.findAll()).hasSize(0);
    }

    @Test
    public void testarUpdateExtratoPoupanca() {
        ExtratoContaCorrente testeUpdate;

        ExtratoContaCorrente ecc = new ExtratoContaCorrente();
        ecc.setDataHoraMovimento(LocalDateTime.now());
        ecc.setOperacao("saque");
        ecc.setValorOperacao(300.0);
        extratoContaCorrenteRepository.saveAndFlush(ecc);

        ecc.setOperacao("deposito");
        ecc.setValorOperacao(100.0);
        extratoContaCorrenteRepository.saveAndFlush(ecc);

        testeUpdate = extratoContaCorrenteRepository.findById(ecc.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testeUpdate.getOperacao()).isEqualTo("deposito");
        assertThat(testeUpdate.getValorOperacao()).isEqualTo(100.0);

    }

    @Test
    public void testarReadExtratoPoupanca() {
        ExtratoContaCorrente ecc = new ExtratoContaCorrente();
        ecc.setDataHoraMovimento(LocalDateTime.now());
        ecc.setOperacao("saque");
        ecc.setValorOperacao(300.0);
        extratoContaCorrenteRepository.saveAndFlush(ecc);

        assertThat(extratoContaCorrenteRepository.findById(ecc.getId()).orElseThrow(NoSuchElementException::new).getValorOperacao()).isEqualTo(300.0);
        assertThat(extratoContaCorrenteRepository.findById(ecc.getId()).orElseThrow(NoSuchElementException::new).getOperacao()).isEqualTo("saque");
    }

    @Test
    public void testarExceptionContaPoupancaNotFound() {
        thrown.expect(ContaCorrenteNotFoundException.class);
        thrown.expectMessage("Nao foi possivel achar a conta Corrente");

        ExtratoContaCorrente ecc = new ExtratoContaCorrente();
        ecc.setDataHoraMovimento(LocalDateTime.now());
        ecc.setOperacao("saque");
        ecc.setValorOperacao(300.0);
        extratoContaCorrenteRepository.saveAndFlush(ecc);

        if (ecc.getContaCorrente() == null) {
            throw new ContaCorrenteNotFoundException("Nao foi possivel achar a conta Corrente");
        }
    }

}
