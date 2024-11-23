package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.model.Cliente;

public class ClienteDAO {
    private Connection minhaConexao;

    public ClienteDAO() throws ClassNotFoundException, SQLException {
        minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserirCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO T_CLIENTE (CLIENTE_ID, USUARIO_ID, NOME_CLIENTE, ENDERECO, TELEFONE) VALUES (SQ_CLIENTE.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[] { "CLIENTE_ID" });
        stmt.setInt(1, cliente.getUsuarioId());
        stmt.setString(2, cliente.getNomeCliente());
        stmt.setString(3, cliente.getEndereco());
        stmt.setString(4, cliente.getTelefone());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int clienteId = -1;
        if (rs.next()) {
            clienteId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return clienteId;
    }

    public Cliente obterClientePorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT CLIENTE_ID, USUARIO_ID, NOME_CLIENTE, ENDERECO, TELEFONE FROM T_CLIENTE WHERE USUARIO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, usuarioId);
        ResultSet rs = stmt.executeQuery();
        Cliente cliente = null;
        if (rs.next()) {
            cliente = new Cliente();
            cliente.setClienteId(rs.getInt("CLIENTE_ID"));
            cliente.setUsuarioId(rs.getInt("USUARIO_ID"));
            cliente.setNomeCliente(rs.getString("NOME_CLIENTE"));
            cliente.setEndereco(rs.getString("ENDERECO"));
            cliente.setTelefone(rs.getString("TELEFONE"));
        }
        rs.close();
        stmt.close();
        return cliente;
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE T_CLIENTE SET NOME_CLIENTE = ?, ENDERECO = ?, TELEFONE = ? WHERE CLIENTE_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, cliente.getNomeCliente());
        stmt.setString(2, cliente.getEndereco());
        stmt.setString(3, cliente.getTelefone());
        stmt.setInt(4, cliente.getClienteId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletarCliente(int clienteId) throws SQLException {
        String sql = "DELETE FROM T_CLIENTE WHERE CLIENTE_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, clienteId);
        stmt.executeUpdate();
        stmt.close();
    }
    public Cliente obterClientePorId(int clienteId) throws SQLException {
        String sql = "SELECT CLIENTE_ID, USUARIO_ID, NOME_CLIENTE, ENDERECO, TELEFONE FROM T_CLIENTE WHERE CLIENTE_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, clienteId);
        ResultSet rs = stmt.executeQuery();
        Cliente cliente = null;
        if (rs.next()) {
            cliente = new Cliente();
            cliente.setClienteId(rs.getInt("CLIENTE_ID"));
            cliente.setUsuarioId(rs.getInt("USUARIO_ID"));
            cliente.setNomeCliente(rs.getString("NOME_CLIENTE"));
            cliente.setEndereco(rs.getString("ENDERECO"));
            cliente.setTelefone(rs.getString("TELEFONE"));
        }
        rs.close();
        stmt.close();
        return cliente;
    }
}