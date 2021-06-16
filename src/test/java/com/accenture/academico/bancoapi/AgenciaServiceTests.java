package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.Agencia;
import com.accenture.academico.bancoapi.service.AgenciaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.accenture.academico.bancoapi.ClienteServicesTest.getConexao;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class AgenciaServiceTests {

    @Autowired
    private AgenciaService agencia_service;

    @Value("${spring.datasource.url}")
    private String url;

    @Test
    public void testarGetAgenciaByID() throws ClassNotFoundException, SQLException {
        Connection conexao = getConexao(url);
        Statement st = conexao.createStatement();
        st.executeQuery("select * from agencia");
        ResultSet rs = st.getResultSet();
        rs.next();

        Agencia agencia = agencia_service.getAgenciaById(rs.getLong(1));

        assertThat(rs.getString(2)).isEqualTo(agencia.getEnderecoAgencia());
        assertThat(rs.getString(3)).isEqualTo(agencia.getFoneAgencia());
        assertThat(rs.getString(4)).isEqualTo(agencia.getNomeAgencia());
    }

    @Test
    public void testarGetAllAgencia() throws ClassNotFoundException, SQLException{
        List<Agencia> agencias = agencia_service.getAgencia();
        Connection conexao = getConexao(url);
        Statement st = conexao.createStatement();

        agencias.forEach(agencia -> {
            try {
                st.executeQuery(String.format("Select endereco_agencia from agencia where id=%s", agencia.getId()));
                st.executeQuery(String.format("Select fone_agencia from agencia where id=%s", agencia.getId()));
                st.executeQuery(String.format("Select nome_agencia from agencia where id=%s", agencia.getId()));

                ResultSet rs = st.getResultSet();

                assertThat(rs.getString(2)).isEqualTo(agencia.getEnderecoAgencia());
                assertThat(rs.getString(3)).isEqualTo(agencia.getFoneAgencia());
                assertThat(rs.getString(4)).isEqualTo(agencia.getNomeAgencia());

            }
            catch(SQLException e){
                e.getMessage();
            }
        });
    }

}
