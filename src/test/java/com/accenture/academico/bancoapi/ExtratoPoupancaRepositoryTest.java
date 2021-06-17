package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.ExtratoContaPoupanca;
import com.accenture.academico.bancoapi.exception.ContaPoupancaNotFoundException;
import com.accenture.academico.bancoapi.repository.ExtratoContaPoupancaRepository;
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
public class ExtratoPoupancaRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private ExtratoContaPoupancaRepository extratoContaPoupancaRepository;

    @Test
    public void testInsertAndDelete() throws Exception {

        assertThat(extratoContaPoupancaRepository).isNotNull();

        ExtratoContaPoupanca ecp = new ExtratoContaPoupanca();
        ecp.setDataHoraMovimento(LocalDateTime.now());
        ecp.setOperacao("saque");
        ecp.setValorOperacao(300.0);
        extratoContaPoupancaRepository.saveAndFlush(ecp);

        assertThat(ecp.getId()).isNotNull();

        ExtratoContaPoupanca ecp2 = extratoContaPoupancaRepository.findById(ecp.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(ecp.getOperacao()).isEqualTo(ecp2.getOperacao());

        assertThat(extratoContaPoupancaRepository.findAll()).hasSize(1);

        extratoContaPoupancaRepository.delete(ecp);

        assertThat(extratoContaPoupancaRepository.findAll()).hasSize(0);
    }

    @Test
    public void testarUpdateExtratoPoupanca() {
        ExtratoContaPoupanca testeUpdate;

        ExtratoContaPoupanca ecp = new ExtratoContaPoupanca();
        ecp.setDataHoraMovimento(LocalDateTime.now());
        ecp.setOperacao("saque");
        ecp.setValorOperacao(300.0);
        extratoContaPoupancaRepository.saveAndFlush(ecp);

        ecp.setOperacao("deposito");
        ecp.setValorOperacao(100.0);
        extratoContaPoupancaRepository.saveAndFlush(ecp);

        testeUpdate = extratoContaPoupancaRepository.findById(ecp.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testeUpdate.getOperacao()).isEqualTo("deposito");
        assertThat(testeUpdate.getValorOperacao()).isEqualTo(100.0);

    }

    @Test
    public void testarReadExtratoPoupanca() {
        ExtratoContaPoupanca ecp = new ExtratoContaPoupanca();
        ecp.setDataHoraMovimento(LocalDateTime.now());
        ecp.setOperacao("saque");
        ecp.setValorOperacao(300.0);
        extratoContaPoupancaRepository.saveAndFlush(ecp);

        assertThat(extratoContaPoupancaRepository.findById(ecp.getId()).orElseThrow(NoSuchElementException::new).getValorOperacao()).isEqualTo(300.0);
        assertThat(extratoContaPoupancaRepository.findById(ecp.getId()).orElseThrow(NoSuchElementException::new).getOperacao()).isEqualTo("saque");
    }

    @Test
    public void testarExceptionContaPoupancaNotFound() {
        thrown.expect(ContaPoupancaNotFoundException.class);
        thrown.expectMessage("Nao foi possivel achar a conta Poupanca");

        ExtratoContaPoupanca ecp = new ExtratoContaPoupanca();
        ecp.setDataHoraMovimento(LocalDateTime.now());
        ecp.setOperacao("saque");
        ecp.setValorOperacao(300.0);
        extratoContaPoupancaRepository.saveAndFlush(ecp);

        if (ecp.getContaPoupanca() == null) {
            throw new ContaPoupancaNotFoundException("Nao foi possivel achar a conta Poupanca");
        }
    }

}
