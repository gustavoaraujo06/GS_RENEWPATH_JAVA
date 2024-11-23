package br.com.fiap.BO;

import java.sql.SQLException;

import br.com.fiap.DAO.ClienteDAO;
import br.com.fiap.model.Cliente;

public class ClienteBO {
    private ClienteDAO clienteDAO;

    public ClienteBO() throws ClassNotFoundException, SQLException {
        clienteDAO = new ClienteDAO();
    }

    public Cliente obterCliente(int usuarioId) throws Exception {
        Cliente cliente = clienteDAO.obterClientePorUsuarioId(usuarioId);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado!");
        }
        return cliente;
    }

    public void atualizarCliente(Cliente cliente, int usuarioId) throws Exception {
        Cliente clienteExistente = clienteDAO.obterClientePorUsuarioId(usuarioId);
        if (clienteExistente == null) {
            throw new Exception("Cliente não encontrado!");
        }
        clienteExistente.setNomeCliente(cliente.getNomeCliente());
        clienteExistente.setEndereco(cliente.getEndereco());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteDAO.atualizarCliente(clienteExistente);
    }
}
