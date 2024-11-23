package br.com.fiap.BO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.fiap.DAO.ClienteDAO;
import br.com.fiap.DAO.EmpresaDAO;
import br.com.fiap.DAO.ServicoDAO;
import br.com.fiap.DAO.SolicitacaoDAO;
import br.com.fiap.model.Cliente;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Servico;
import br.com.fiap.model.Solicitacao;

public class SolicitacaoBO {
    private SolicitacaoDAO solicitacaoDAO;
    private ClienteDAO clienteDAO;
    private ServicoDAO servicoDAO;
    private EmpresaDAO empresaDAO;

    public SolicitacaoBO() throws ClassNotFoundException, SQLException {
        solicitacaoDAO = new SolicitacaoDAO();
        clienteDAO = new ClienteDAO();
        servicoDAO = new ServicoDAO();
        empresaDAO = new EmpresaDAO();
    }

    public void criarSolicitacao(int servicoId, int usuarioId) throws Exception {
        Cliente cliente = clienteDAO.obterClientePorUsuarioId(usuarioId);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado!");
        }
        Servico servico = servicoDAO.obterServicoPorId(servicoId);
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        if (solicitacaoDAO.existeSolicitacaoPendente(cliente.getClienteId(), servicoId)) {
            throw new Exception("Você já fez uma solicitação por esse serviço, por favor aguarde a empresa entrar em contato");
        }
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setClienteId(cliente.getClienteId());
        solicitacao.setServicoId(servicoId);
        solicitacao.setDataSolicitacao(new Date());
        solicitacao.setStatus("PENDENTE");
        solicitacaoDAO.inserirSolicitacao(solicitacao);
    }

    public List<Solicitacao> obterSolicitacoes(int usuarioId, int role) throws Exception {
        if (role == 0) {
            Cliente cliente = clienteDAO.obterClientePorUsuarioId(usuarioId);
            if (cliente == null) {
                throw new Exception("Cliente não encontrado!");
            }
            return solicitacaoDAO.obterSolicitacoesPorClienteId(cliente.getClienteId());
        } else if (role == 1) {
            Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
            if (empresa == null) {
                throw new Exception("Empresa não encontrada!");
            }
            return solicitacaoDAO.obterSolicitacoesPorEmpresaId(empresa.getEmpresaId());
        } else {
            throw new Exception("Role inválido!");
        }
    }

    public void atualizarSolicitacao(int solicitacaoId, String novoStatus, int usuarioId) throws Exception {
        Solicitacao solicitacao = solicitacaoDAO.obterSolicitacaoPorId(solicitacaoId);
        if (solicitacao == null) {
            throw new Exception("Solicitação não encontrada!");
        }
        Servico servico = servicoDAO.obterServicoPorId(solicitacao.getServicoId());
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null || servico.getEmpresaId() != empresa.getEmpresaId()) {
            throw new Exception("Você não tem permissão para atualizar esta solicitação!");
        }
        if (!novoStatus.equals("PENDENTE") && !novoStatus.equals("CONFIRMADO")) {
            throw new Exception("Status inválido!");
        }
        solicitacaoDAO.atualizarStatusSolicitacao(solicitacaoId, novoStatus);
    }

    public List<Solicitacao> exportarSolicitacoes(int usuarioId, int role) throws Exception {
        return obterSolicitacoes(usuarioId, role);
    }


    public Servico obterServicoPorId(int servicoId) throws Exception {
        Servico servico = servicoDAO.obterServicoPorId(servicoId);
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        return servico;
    }

    public Empresa obterEmpresaPorServicoId(int servicoId) throws Exception {
        Servico servico = servicoDAO.obterServicoPorId(servicoId);
        if (servico == null) {
            throw new Exception("Serviço não encontrado!");
        }
        Empresa empresa = empresaDAO.obterEmpresaPorId(servico.getEmpresaId());
        if (empresa == null) {
            throw new Exception("Empresa não encontrada!");
        }
        return empresa;
    }

    public Cliente obterClientePorId(int clienteId) throws Exception {
        Cliente cliente = clienteDAO.obterClientePorId(clienteId);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado!");
        }
        return cliente;
    }
}