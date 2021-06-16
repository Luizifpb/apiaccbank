package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.CampoObrigatorioEmptyException;
import com.accenture.academico.bancoapi.exception.ClienteNotFoundException;
import com.accenture.academico.bancoapi.model.ClienteModel;
import com.accenture.academico.bancoapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    AgenciaService agenciaService;

    //getting all clients record
    public List<Cliente> getAllCliente()
    {
        List<Cliente> clientes = new ArrayList<Cliente>();
        clienteRepository.findAll().forEach(cliente -> clientes.add(cliente));
        return clientes;
    }
    //getting a specific record
    public Cliente getClienteById(long id) throws ClienteNotFoundException
    {
        var clienteRetorno = clienteRepository.findById(id);
        if(clienteRetorno.isEmpty()){
            throw new ClienteNotFoundException("Cliente não encontrado.");
        }
        return clienteRetorno.get();
    }

    public Cliente saveOrUpdate(ClienteModel clienteModel) throws AgenciaNotFoundException
    {
        var agenciaRetorno = agenciaService.getAgenciaById(clienteModel.getAgenciaModelId().getId());

        var agencia = new Agencia(clienteModel.getAgenciaModelId().getId(), null, null, null);
        var cliente = new Cliente(null, clienteModel.getNome(), clienteModel.getCpf(), clienteModel.getFone(), agencia);

        if(cliente.getNomeCliente().isEmpty() || cliente.getCpfCliente().isEmpty() || cliente.getFoneCliente().isEmpty()){
            throw new CampoObrigatorioEmptyException("Campo obrigatório vazio.");
        }

        var clienteRetorno = clienteRepository.save(cliente);

        clienteRetorno.setAgencia(agenciaRetorno);
        return clienteRetorno;
    }
    //deleting a specific record
    public Boolean delete(long id) throws ClienteNotFoundException
    {
        var clienteRetorno = clienteRepository.findById(id);
        if(clienteRetorno.isEmpty()){
            throw new ClienteNotFoundException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);

        return true;
    }
}
