package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.model.Solicitacao;

public class SolicitacaoDAO {
    private Connection minhaConexao;

    public SolicitacaoDAO() throws ClassNotFoundException, SQLException {
        minhaConexao = new ConexaoFactory().conexao();
    }

    public int inserirSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO T_SOLICITACAO (SOLICITACAO_ID, CLIENTE_ID, SERVICO_ID, DATA_SOLICITACAO, STATUS) VALUES (SQ_SOLICITACAO.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql, new String[] { "SOLICITACAO_ID" });
        stmt.setInt(1, solicitacao.getClienteId());
        stmt.setInt(2, solicitacao.getServicoId());
        stmt.setTimestamp(3, new Timestamp(solicitacao.getDataSolicitacao().getTime()));
        stmt.setString(4, solicitacao.getStatus());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int solicitacaoId = -1;
        if (rs.next()) {
            solicitacaoId = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return solicitacaoId;
    }

    public List<Solicitacao> obterSolicitacoesPorClienteId(int clienteId) throws SQLException {
        String sql = "SELECT SOLICITACAO_ID, CLIENTE_ID, SERVICO_ID, DATA_SOLICITACAO, STATUS FROM T_SOLICITACAO WHERE CLIENTE_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, clienteId);
        ResultSet rs = stmt.executeQuery();
        List<Solicitacao> solicitacoes = new ArrayList<>();
        while (rs.next()) {
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setSolicitacaoId(rs.getInt("SOLICITACAO_ID"));
            solicitacao.setClienteId(rs.getInt("CLIENTE_ID"));
            solicitacao.setServicoId(rs.getInt("SERVICO_ID"));
            solicitacao.setDataSolicitacao(rs.getTimestamp("DATA_SOLICITACAO"));
            solicitacao.setStatus(rs.getString("STATUS"));
            solicitacoes.add(solicitacao);
        }
        rs.close();
        stmt.close();
        return solicitacoes;
    }

    public List<Solicitacao> obterSolicitacoesPorEmpresaId(int empresaId) throws SQLException {
        String sql = "SELECT SOL.SOLICITACAO_ID, SOL.CLIENTE_ID, SOL.SERVICO_ID, SOL.DATA_SOLICITACAO, SOL.STATUS " +
                "FROM T_SOLICITACAO SOL " +
                "JOIN T_SERVICO S ON SOL.SERVICO_ID = S.SERVICO_ID " +
                "WHERE S.EMPRESA_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, empresaId);
        ResultSet rs = stmt.executeQuery();
        List<Solicitacao> solicitacoes = new ArrayList<>();
        while (rs.next()) {
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setSolicitacaoId(rs.getInt("SOLICITACAO_ID"));
            solicitacao.setClienteId(rs.getInt("CLIENTE_ID"));
            solicitacao.setServicoId(rs.getInt("SERVICO_ID"));
            solicitacao.setDataSolicitacao(rs.getTimestamp("DATA_SOLICITACAO"));
            solicitacao.setStatus(rs.getString("STATUS"));
            solicitacoes.add(solicitacao);
        }
        rs.close();
        stmt.close();
        return solicitacoes;
    }

    public void atualizarStatusSolicitacao(int solicitacaoId, String novoStatus) throws SQLException {
        String sql = "UPDATE T_SOLICITACAO SET STATUS = ? WHERE SOLICITACAO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, novoStatus);
        stmt.setInt(2, solicitacaoId);
        stmt.executeUpdate();
        stmt.close();
    }

    public boolean existeSolicitacaoPendente(int clienteId, int servicoId) throws SQLException {
        String sql = "SELECT 1 FROM T_SOLICITACAO WHERE CLIENTE_ID = ? AND SERVICO_ID = ? AND STATUS = 'PENDENTE'";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, clienteId);
        stmt.setInt(2, servicoId);
        ResultSet rs = stmt.executeQuery();
        boolean existe = rs.next();
        rs.close();
        stmt.close();
        return existe;
    }

    public boolean servicoPossuiSolicitacoesPendentes(int servicoId) throws SQLException {
        String sql = "SELECT 1 FROM T_SOLICITACAO WHERE SERVICO_ID = ? AND STATUS = 'PENDENTE'";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, servicoId);
        ResultSet rs = stmt.executeQuery();
        boolean existe = rs.next();
        rs.close();
        stmt.close();
        return existe;
    }

    public Solicitacao obterSolicitacaoPorId(int solicitacaoId) throws SQLException {
        String sql = "SELECT SOLICITACAO_ID, CLIENTE_ID, SERVICO_ID, DATA_SOLICITACAO, STATUS FROM T_SOLICITACAO WHERE SOLICITACAO_ID = ?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, solicitacaoId);
        ResultSet rs = stmt.executeQuery();
        Solicitacao solicitacao = null;
        if (rs.next()) {
            solicitacao = new Solicitacao();
            solicitacao.setSolicitacaoId(rs.getInt("SOLICITACAO_ID"));
            solicitacao.setClienteId(rs.getInt("CLIENTE_ID"));
            solicitacao.setServicoId(rs.getInt("SERVICO_ID"));
            solicitacao.setDataSolicitacao(rs.getTimestamp("DATA_SOLICITACAO"));
            solicitacao.setStatus(rs.getString("STATUS"));
        }
        rs.close();
        stmt.close();
        return solicitacao;
    }
}