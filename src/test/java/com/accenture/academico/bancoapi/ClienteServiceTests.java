package com.accenture.academico.bancoapi;

import com.accenture.academico.bancoapi.entity.Cliente;
import com.accenture.academico.bancoapi.model.AgenciaModelId;
import com.accenture.academico.bancoapi.model.ClienteModel;
import com.accenture.academico.bancoapi.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
class ClienteServicesTest {
	
	@Autowired
	private ClienteService cliente_service;

	@Value("${spring.datasource.url}")
	private String url;

	private static Cliente Cliente_Para_Teste;

	public static Connection getConexao(String URL) throws ClassNotFoundException {
		Connection conexao = null;
		if (conexao == null) {
			try {
				String url = URL;
				String user = "root";
				String pass = "root";
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn = DriverManager.getConnection(url, user, pass);
				cn.setAutoCommit(false);
				System.out.println("Connection successfully established! \n");
				return cn;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conexao;
	}


	
	@Test
	public void testarCadastroCliente() {
		AgenciaModelId agenciamodelid = new AgenciaModelId(1L);
		ClienteModel modelcliente = new ClienteModel("José", "975.263.880-52", "(87) 9 9123-4586", agenciamodelid );

		Cliente cliente = cliente_service.saveOrUpdate(modelcliente);
		
		assertThat(cliente.getNomeCliente()).isEqualTo("José");
		assertThat(cliente.getCpfCliente()).isEqualTo("975.263.880-52");
		assertThat(cliente.getFoneCliente()).isEqualTo("(87) 9 9123-4586");
		assertThat(cliente.getAgencia().getId()).isEqualTo(1L);	
	}

	@Test
	public void testarUpdateCliente() {
		AgenciaModelId agenciamodelid = new AgenciaModelId(1L);
		ClienteModel modelcliente = new ClienteModel("Maria", "299.722.910-30", "(87) 9 9123-4575", agenciamodelid );

		Cliente cliente = cliente_service.saveOrUpdate(modelcliente);
		Cliente_Para_Teste = cliente;

		assertThat(cliente.getNomeCliente()).isEqualTo("Maria");
		assertThat(cliente.getCpfCliente()).isEqualTo("299.722.910-30");
		assertThat(cliente.getFoneCliente()).isEqualTo("(87) 9 9123-4575");
		assertThat(cliente.getAgencia().getId()).isEqualTo(1L);
	}
	
	@Test
	public void testarGetClienteEspecifico(){

		Cliente cliente = cliente_service.getClienteById(Cliente_Para_Teste.getId());

		assertThat(cliente.getNomeCliente()).isEqualTo(Cliente_Para_Teste.getNomeCliente());
		assertThat(cliente.getCpfCliente()).isEqualTo(Cliente_Para_Teste.getCpfCliente());
		assertThat(cliente.getFoneCliente()).isEqualTo(Cliente_Para_Teste.getFoneCliente());
		assertThat(cliente.getAgencia().getId()).isEqualTo(Cliente_Para_Teste.getAgencia().getId());
	}

    @Test
    public void testarGetAll() throws ClassNotFoundException, SQLException {
		List<Cliente> clientes = cliente_service.getAllCliente();
        Connection conexao = getConexao(url);
		Statement st = conexao.createStatement();

		clientes.forEach(cliente -> {
			try {
				st.executeQuery(String.format("Select nome_cliente from cliente where id=%s", cliente.getId()));
				st.executeQuery(String.format("Select cpf_cliente from cliente where id=%s", cliente.getId()));
				st.executeQuery(String.format("Select fone_cliente from cliente where id=%s", cliente.getId()));
				st.executeQuery(String.format("Select agencia_id from cliente where id=%s", cliente.getId()));

				ResultSet rs = st.getResultSet();

				assertThat(rs.getString(1)).isEqualTo(cliente.getNomeCliente());
				assertThat(rs.getString(2)).isEqualTo(cliente.getCpfCliente());
				assertThat(rs.getString(3)).isEqualTo(cliente.getFoneCliente());
				assertThat(rs.getString(4)).isEqualTo(cliente.getAgencia().getId());

			}
			catch(SQLException e){
				e.getMessage();
			}
		});

    }

    @Test
	public void testardeletarCliente(){

		assertThat(cliente_service.delete(Cliente_Para_Teste.getId())).isEqualTo(true);

	}

}
