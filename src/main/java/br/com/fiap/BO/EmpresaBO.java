package br.com.fiap.BO;

import java.sql.SQLException;

import br.com.fiap.DAO.EmpresaDAO;
import br.com.fiap.DAO.UsuarioDAO;
import br.com.fiap.model.Empresa;

public class EmpresaBO {
    private EmpresaDAO empresaDAO;
    public EmpresaBO() throws ClassNotFoundException, SQLException {
        empresaDAO = new EmpresaDAO();
        new UsuarioDAO();
    }

    public Empresa obterEmpresa(int usuarioId) throws Exception {
        Empresa empresa = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresa == null) {
            throw new Exception("Empresa não encontrada!");
        }
        return empresa;
    }

    public void atualizarEmpresa(Empresa empresa, int usuarioId) throws Exception {
        Empresa empresaExistente = empresaDAO.obterEmpresaPorUsuarioId(usuarioId);
        if (empresaExistente == null) {
            throw new Exception("Empresa não encontrada!");
        }
        empresaExistente.setNomeEmpresa(empresa.getNomeEmpresa());
        empresaExistente.setEndereco(empresa.getEndereco());
        empresaExistente.setTelefone(empresa.getTelefone());
        empresaDAO.atualizarEmpresa(empresaExistente);
    }
}