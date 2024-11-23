package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.model.Usuario;

public class UsuarioDAO {
    private Connection minhaConexao;

    public UsuarioDAO() throws ClassNotFoundException, SQLException {
        minhaConexao = new ConexaoFactory().conexao();
    }

    public int registrarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO T_USUARIO (USUARIO_ID, EMAIL, SENHA, USUARIO_ROLE) VALUES (SQ_USUARIO.NEXTVAL, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[] { "USUARIO_ID" });
        stmt.setString(1, usuario.getEmail());
        stmt.setString(2, usuario.getSenha());
        stmt.setInt(3, usuario.getUsuarioRole());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int usuarioId = -1;
        if (rs.next()) {
            usuarioId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return usuarioId;
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM T_USUARIO WHERE EMAIL = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        boolean existe = rs.next();
        rs.close();
        stmt.close();
        return existe;
    }

    public Usuario obterUsuarioPorEmailSenha(String email, String senha) throws SQLException {
        String sql = "SELECT USUARIO_ID, EMAIL, SENHA, USUARIO_ROLE FROM T_USUARIO WHERE EMAIL = ? AND SENHA = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, senha);
        ResultSet rs = stmt.executeQuery();
        Usuario usuario = null;
        if (rs.next()) {
            usuario = new Usuario();
            usuario.setUsuarioId(rs.getInt("USUARIO_ID"));
            usuario.setEmail(rs.getString("EMAIL"));
            usuario.setSenha(rs.getString("SENHA"));
            usuario.setUsuarioRole(rs.getInt("USUARIO_ROLE"));
        }
        rs.close();
        stmt.close();
        return usuario;
    }

    public Usuario obterUsuarioPorId(int usuarioId) throws SQLException {
        String sql = "SELECT USUARIO_ID, EMAIL, SENHA, USUARIO_ROLE FROM T_USUARIO WHERE USUARIO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, usuarioId);
        ResultSet rs = stmt.executeQuery();
        Usuario usuario = null;
        if (rs.next()) {
            usuario = new Usuario();
            usuario.setUsuarioId(rs.getInt("USUARIO_ID"));
            usuario.setEmail(rs.getString("EMAIL"));
            usuario.setSenha(rs.getString("SENHA"));
            usuario.setUsuarioRole(rs.getInt("USUARIO_ROLE"));
        }
        rs.close();
        stmt.close();
        return usuario;
    }

    public void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE T_USUARIO SET EMAIL = ?, SENHA = ? WHERE USUARIO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, usuario.getEmail());
        stmt.setString(2, usuario.getSenha());
        stmt.setInt(3, usuario.getUsuarioId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletarUsuario(int usuarioId) throws SQLException {
        String sql = "DELETE FROM T_USUARIO WHERE USUARIO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, usuarioId);
        stmt.executeUpdate();
        stmt.close();
    }
}

