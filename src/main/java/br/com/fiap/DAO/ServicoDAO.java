package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.model.Servico;

public class ServicoDAO {
    private Connection minhaConexao;

    public ServicoDAO() throws ClassNotFoundException, SQLException {
        minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserirServico(Servico servico) throws SQLException {
        String sql = "INSERT INTO T_SERVICO (SERVICO_ID, EMPRESA_ID, NOME_SERVICO, DESCRICAO, PRECO) VALUES (SQ_SERVICO.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[] { "SERVICO_ID" });
        stmt.setInt(1, servico.getEmpresaId());
        stmt.setString(2, servico.getNomeServico());
        stmt.setString(3, servico.getDescricao());
        stmt.setDouble(4, servico.getPreco());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int servicoId = -1;
        if (rs.next()) {
            servicoId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return servicoId;
    }

    public List<Servico> obterServicosPorEmpresaId(int empresaId) throws SQLException {
        String sql = "SELECT SERVICO_ID, EMPRESA_ID, NOME_SERVICO, DESCRICAO, PRECO FROM T_SERVICO WHERE EMPRESA_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, empresaId);
        ResultSet rs = stmt.executeQuery();
        List<Servico> servicos = new ArrayList<>();
        while (rs.next()) {
            Servico servico = new Servico();
            servico.setServicoId(rs.getInt("SERVICO_ID"));
            servico.setEmpresaId(rs.getInt("EMPRESA_ID"));
            servico.setNomeServico(rs.getString("NOME_SERVICO"));
            servico.setDescricao(rs.getString("DESCRICAO"));
            servico.setPreco(rs.getDouble("PRECO"));
            servicos.add(servico);
        }
        rs.close();
        stmt.close();
        return servicos;
    }

    public List<Servico> obterServicosComFiltros(String nomeServico, Double minPreco, Double maxPreco, String orderBy, Boolean descending) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT S.SERVICO_ID, S.NOME_SERVICO, S.DESCRICAO, S.PRECO, E.EMPRESA_ID FROM T_SERVICO S JOIN T_EMPRESA E ON S.EMPRESA_ID = E.EMPRESA_ID WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (nomeServico != null && !nomeServico.isEmpty()) {
            sql.append(" AND LOWER(S.NOME_SERVICO) LIKE ?");
            params.add("%" + nomeServico.toLowerCase() + "%");
        }
        if (minPreco != null) {
            sql.append(" AND S.PRECO >= ?");
            params.add(minPreco);
        }
        if (maxPreco != null) {
            sql.append(" AND S.PRECO <= ?");
            params.add(maxPreco);
        }
        if (orderBy != null && (orderBy.equals("nome_servico") || orderBy.equals("preco"))) {
            sql.append(" ORDER BY ").append(orderBy);
            if (descending != null && descending) {
                sql.append(" DESC");
            }
        }

        PreparedStatement stmt = minhaConexao.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();
        List<Servico> servicos = new ArrayList<>();
        while (rs.next()) {
            Servico servico = new Servico();
            servico.setServicoId(rs.getInt("SERVICO_ID"));
            servico.setNomeServico(rs.getString("NOME_SERVICO"));
            servico.setDescricao(rs.getString("DESCRICAO"));
            servico.setPreco(rs.getDouble("PRECO"));
            servico.setEmpresaId(rs.getInt("EMPRESA_ID"));
            servicos.add(servico);
        }
        rs.close();
        stmt.close();
        return servicos;
    }

    public Servico obterServicoPorId(int servicoId) throws SQLException {
        String sql = "SELECT SERVICO_ID, EMPRESA_ID, NOME_SERVICO, DESCRICAO, PRECO FROM T_SERVICO WHERE SERVICO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, servicoId);
        ResultSet rs = stmt.executeQuery();
        Servico servico = null;
        if (rs.next()) {
            servico = new Servico();
            servico.setServicoId(rs.getInt("SERVICO_ID"));
            servico.setEmpresaId(rs.getInt("EMPRESA_ID"));
            servico.setNomeServico(rs.getString("NOME_SERVICO"));
            servico.setDescricao(rs.getString("DESCRICAO"));
            servico.setPreco(rs.getDouble("PRECO"));
        }
        rs.close();
        stmt.close();
        return servico;
    }

    public void atualizarServico(Servico servico) throws SQLException {
        String sql = "UPDATE T_SERVICO SET NOME_SERVICO = ?, DESCRICAO = ?, PRECO = ? WHERE SERVICO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, servico.getNomeServico());
        stmt.setString(2, servico.getDescricao());
        stmt.setDouble(3, servico.getPreco());
        stmt.setInt(4, servico.getServicoId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletarServico(int servicoId) throws SQLException {
        String sql = "DELETE FROM T_SERVICO WHERE SERVICO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, servicoId);
        stmt.executeUpdate();
        stmt.close();
    }
}