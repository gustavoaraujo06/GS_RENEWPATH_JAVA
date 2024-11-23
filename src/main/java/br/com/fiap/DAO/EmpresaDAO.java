package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.model.Empresa;

public class EmpresaDAO {
    private Connection minhaConexao;

    public EmpresaDAO() throws ClassNotFoundException, SQLException {
        minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserirEmpresa(Empresa empresa) throws SQLException {
        String sql = "INSERT INTO T_EMPRESA (EMPRESA_ID, USUARIO_ID, NOME_EMPRESA, ENDERECO, TELEFONE) VALUES (SQ_EMPRESA.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[] { "EMPRESA_ID" });
        stmt.setInt(1, empresa.getUsuarioId());
        stmt.setString(2, empresa.getNomeEmpresa());
        stmt.setString(3, empresa.getEndereco());
        stmt.setString(4, empresa.getTelefone());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int empresaId = -1;
        if (rs.next()) {
            empresaId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return empresaId;
    }

    public Empresa obterEmpresaPorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT EMPRESA_ID, USUARIO_ID, NOME_EMPRESA, ENDERECO, TELEFONE FROM T_EMPRESA WHERE USUARIO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, usuarioId);
        ResultSet rs = stmt.executeQuery();
        Empresa empresa = null;
        if (rs.next()) {
            empresa = new Empresa();
            empresa.setEmpresaId(rs.getInt("EMPRESA_ID"));
            empresa.setUsuarioId(rs.getInt("USUARIO_ID"));
            empresa.setNomeEmpresa(rs.getString("NOME_EMPRESA"));
            empresa.setEndereco(rs.getString("ENDERECO"));
            empresa.setTelefone(rs.getString("TELEFONE"));
        }
        rs.close();
        stmt.close();
        return empresa;
    }

    public void atualizarEmpresa(Empresa empresa) throws SQLException {
        String sql = "UPDATE T_EMPRESA SET NOME_EMPRESA = ?, ENDERECO = ?, TELEFONE = ? WHERE EMPRESA_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, empresa.getNomeEmpresa());
        stmt.setString(2, empresa.getEndereco());
        stmt.setString(3, empresa.getTelefone());
        stmt.setInt(4, empresa.getEmpresaId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletarEmpresa(int empresaId) throws SQLException {
        String sql = "DELETE FROM T_EMPRESA WHERE EMPRESA_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, empresaId);
        stmt.executeUpdate();
        stmt.close();
    }
    public Empresa obterEmpresaPorId(int empresaId) throws SQLException {
        String sql = "SELECT EMPRESA_ID, USUARIO_ID, NOME_EMPRESA, ENDERECO, TELEFONE FROM T_EMPRESA WHERE EMPRESA_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, empresaId);
        ResultSet rs = stmt.executeQuery();
        Empresa empresa = null;
        if (rs.next()) {
            empresa = new Empresa();
            empresa.setEmpresaId(rs.getInt("EMPRESA_ID"));
            empresa.setUsuarioId(rs.getInt("USUARIO_ID"));
            empresa.setNomeEmpresa(rs.getString("NOME_EMPRESA"));
            empresa.setEndereco(rs.getString("ENDERECO"));
            empresa.setTelefone(rs.getString("TELEFONE"));
        }
        rs.close();
        stmt.close();
        return empresa;
    }
}
