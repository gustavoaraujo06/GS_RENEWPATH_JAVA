package br.com.fiap.BO;

import java.sql.SQLException;
import java.util.List;

import br.com.fiap.DAO.EmpresaDAO;
import br.com.fiap.DAO.ServicoDAO;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Servico;

public class ServicoBO {
    private ServicoDAO servicoDAO;
    private EmpresaDAO empresaDAO;

    public ServicoBO() throws ClassNotFoundException, SQLException {
        servicoDAO = new ServicoDAO();
        empresaDAO = new EmpresaDAO();
    }

    public void adicionarServico(Servico servico, int usuarioId) throws Exception {
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null) {
            throw new Exception("Empresa não encontrada!");
        }
        servico.setEmpresaId(empresa.getEmpresaId());
        if (servico.getPreco() <= 0) {
            throw new Exception("Preço inválido!");
        }
        servicoDAO.inserirServico(servico);
    }

    public List<Servico> obterServicosComFiltros(String nomeServico, Double minPreco, Double maxPreco, String orderBy, Boolean descending) throws SQLException {
        return servicoDAO.obterServicosComFiltros(nomeServico, minPreco, maxPreco, orderBy, descending);
    }

    public Servico obterServicoPorId(int servicoId, int usuarioId) throws Exception {
        Servico servico = servicoDAO.obterServicoPorId(servicoId);
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null || servico.getEmpresaId() != empresa.getEmpresaId()) {
            throw new Exception("Você não tem permissão para visualizar este serviço!");
        }
        return servico;
    }

    public void atualizarServico(Servico servico, int usuarioId) throws Exception {
        Servico servicoExistente = servicoDAO.obterServicoPorId(servico.getServicoId());
        if (servicoExistente == null) {
            throw new Exception("Serviço não encontrado!");
        }
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null || servicoExistente.getEmpresaId() != empresa.getEmpresaId()) {
            throw new Exception("Você não tem permissão para atualizar este serviço!");
        }
        servicoExistente.setNomeServico(servico.getNomeServico());
        servicoExistente.setDescricao(servico.getDescricao());
        servicoExistente.setPreco(servico.getPreco());
        servicoDAO.atualizarServico(servicoExistente);
    }

    public void deletarServico(int servicoId, int usuarioId) throws Exception {
        Servico servico = servicoDAO.obterServicoPorId(servicoId);
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null || servico.getEmpresaId() != empresa.getEmpresaId()) {
            throw new Exception("Você não tem permissão para deletar este serviço!");
        }
        servicoDAO.deletarServico(servicoId);
    }

    public List<Servico> obterServicosDaEmpresa(int usuarioId) throws Exception {
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null) {
            throw new Exception("Empresa não encontrada!");
        }
        return servicoDAO.obterServicosPorEmpresaId(empresa.getEmpresaId());
    }
}