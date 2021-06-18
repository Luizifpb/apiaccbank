package com.accenture.academico.bancoapi.service;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.exception.AgenciaNotFoundException;
import com.accenture.academico.bancoapi.exception.CampoObrigatorioEmptyException;
import com.accenture.academico.bancoapi.model.AgenciaModel;
import com.accenture.academico.bancoapi.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgenciaService {
    @Autowired
    AgenciaRepository agenciaRepository;

    public Agencia getAgenciaById(long id) {
        var agenciaRetorno = agenciaRepository.findById(id);
        if (agenciaRetorno.isEmpty()) {
            throw new AgenciaNotFoundException("Agência não encontrada.");
        }
        return agenciaRetorno.get();
    }

    public List<Agencia> getAllAgencia() {
        List<Agencia> agencias = new ArrayList<Agencia>();
        agenciaRepository.findAll().forEach(agencia -> agencias.add(agencia));
        return agencias;
    }

    public Agencia saveOrUpdate(AgenciaModel agenciaModel) throws CampoObrigatorioEmptyException {
        var agencia = new Agencia(null, agenciaModel.getNomeAgencia(), agenciaModel.getEnderecoAgencia(), agenciaModel.getFoneAgencia());

        if (agenciaModel.getNomeAgencia().isEmpty() || agenciaModel.getEnderecoAgencia().isEmpty() || agenciaModel.getFoneAgencia().isEmpty()) {
            throw new CampoObrigatorioEmptyException("Campo obrigatório vazio.");
        }

        agencia = agenciaRepository.save(agencia);

        return agencia;
    }

}
