package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.repository.AgenciaRepository;
import org.junit.Test;
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
public class AgenciaRepositoryTest {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Test
    public void testInsertAndDeleteAgencia() throws Exception {

        assertThat(agenciaRepository).isNotNull();

        Agencia a = new Agencia();
        a.setEnderecoAgencia("Rua: 7 de Setembro");
        a.setFoneAgencia("(83) 3 3374-1100");
        a.setNomeAgencia("Nubank");

        agenciaRepository.saveAndFlush(a);

        assertThat(a.getId()).isNotNull();

        Agencia a2 = agenciaRepository.findById(a.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(a.getId()).isEqualTo(a2.getId());

        assertThat(agenciaRepository.findAll()).hasSize(1);

        agenciaRepository.delete(a);

        assertThat(agenciaRepository.findAll()).hasSize(0);
    }

    @Test
    public void testarUpdateAgencia() {
        Agencia testeUpdate;

        Agencia ag = new Agencia();
        ag.setEnderecoAgencia("Rua: 7 de Setembro");
        ag.setFoneAgencia("(83) 3 3374-1100");
        ag.setNomeAgencia("Nubank");
        agenciaRepository.saveAndFlush(ag);

        ag.setFoneAgencia("(83) 3 3374-0000");
        ag.setNomeAgencia("Itau");
        agenciaRepository.saveAndFlush(ag);

        testeUpdate = agenciaRepository.findById(ag.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testeUpdate.getFoneAgencia()).isEqualTo("(83) 3 3374-0000");
        assertThat(testeUpdate.getNomeAgencia()).isEqualTo("Itau");
    }

    @Test
    public void testarReadAgencia() {
        Agencia ag = new Agencia();
        ag.setEnderecoAgencia("Rua: 7 de Setembro");
        ag.setFoneAgencia("(83) 3 3374-1100");
        ag.setNomeAgencia("Ivonete");
        agenciaRepository.saveAndFlush(ag);

        assertThat(agenciaRepository.findById(ag.getId()).orElseThrow(NoSuchElementException::new).getEnderecoAgencia()).isEqualTo("Rua: 7 de Setembro");
        assertThat(agenciaRepository.findById(ag.getId()).orElseThrow(NoSuchElementException::new).getFoneAgencia()).isEqualTo("(83) 3 3374-1100");
        assertThat(agenciaRepository.findById(ag.getId()).orElseThrow(NoSuchElementException::new).getNomeAgencia()).isEqualTo("Ivonete");
    }
}
