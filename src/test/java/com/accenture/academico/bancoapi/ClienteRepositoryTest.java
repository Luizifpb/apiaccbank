package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.repository.ClienteRepository;
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
public class ClienteRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testInsertAndDeleteCliente() throws Exception {

        assertThat(clienteRepository).isNotNull();

        Cliente c1 = new Cliente();
        c1.setCpfCliente("106.213.514-82");
        c1.setFoneCliente("(83) 9 9654-7153");
        c1.setNomeCliente("Mateus");

        clienteRepository.saveAndFlush(c1);

        assertThat(c1.getId()).isNotNull();

        Cliente c2 = clienteRepository.findById(c1.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(c1.getId()).isEqualTo(c2.getId());

        assertThat(clienteRepository.findAll()).hasSize(1);

        clienteRepository.delete(c1);

        assertThat(clienteRepository.findAll()).hasSize(0);
    }

    @Test
    public void testarUpdateCliente() {
        Cliente testarUpdate;

        Cliente c1 = new Cliente();
        c1.setCpfCliente("106.213.514-82");
        c1.setFoneCliente("(83) 9 9654-7153");
        c1.setNomeCliente("Luiz");
        clienteRepository.saveAndFlush(c1);

        c1.setFoneCliente("(83) 9 1111-0000");
        c1.setNomeCliente("Michael Jackson");
        clienteRepository.saveAndFlush(c1);

        testarUpdate = clienteRepository.findById(c1.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(testarUpdate.getFoneCliente()).isEqualTo("(83) 9 1111-0000");
        assertThat(testarUpdate.getNomeCliente()).isEqualTo("Michael Jackson");
    }

    @Test
    public void testarReadCliente() {
        Cliente c1 = new Cliente();
        c1.setCpfCliente("106.213.514-82");
        c1.setFoneCliente("(83) 9 9654-7153");
        c1.setNomeCliente("Wellison");
        clienteRepository.saveAndFlush(c1);

        assertThat(clienteRepository.findById(c1.getId()).orElseThrow(NoSuchElementException::new).getCpfCliente()).isEqualTo("106.213.514-82");
        assertThat(clienteRepository.findById(c1.getId()).orElseThrow(NoSuchElementException::new).getFoneCliente()).isEqualTo("(83) 9 9654-7153");
        assertThat(clienteRepository.findById(c1.getId()).orElseThrow(NoSuchElementException::new).getNomeCliente()).isEqualTo("Wellison");
    }

    @Test
    public void testarAgenciaNotFoundException() {
        thrown.expect(AgenciaNotFoundException.class);
        thrown.expectMessage("Agencia nao pode ser encontrada");

        Cliente c1 = new Cliente();
        c1.setCpfCliente("106.213.514-82");
        c1.setFoneCliente("(83) 9 9654-7153");
        c1.setNomeCliente("Ian");

        if (c1.getAgencia() == null) {
            throw new AgenciaNotFoundException("Agencia nao pode ser encontrada");
        }
    }
}
