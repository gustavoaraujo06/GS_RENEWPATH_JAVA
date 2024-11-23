package br.com.fiap.BO;

import java.sql.SQLException;

import br.com.fiap.DAO.ClienteDAO;
import br.com.fiap.DAO.EmpresaDAO;
import br.com.fiap.DAO.UsuarioDAO;
import br.com.fiap.model.Cliente;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Usuario;
import br.com.fiap.util.HashUtil;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;
    private EmpresaDAO empresaDAO;
    private ClienteDAO clienteDAO;

    public UsuarioBO() throws ClassNotFoundException, SQLException {
        usuarioDAO = new UsuarioDAO();
        empresaDAO = new EmpresaDAO();
        clienteDAO = new ClienteDAO();
    }

    public void registrarUsuario(Usuario usuario, String nome, String telefone, String endereco) throws Exception {
        if (usuarioDAO.emailExiste(usuario.getEmail())) {
            throw new Exception("Email já cadastrado!");
        }
        if (usuario.getUsuarioRole() != 0 && usuario.getUsuarioRole() != 1) {
            throw new Exception("Role inválido!");
        }
        String senhaHash = HashUtil.hashSenha(usuario.getSenha());
        usuario.setSenha(senhaHash);
        int usuarioId = usuarioDAO.registrarUsuario(usuario);
        usuario.setUsuarioId(usuarioId);

        if (usuario.getUsuarioRole() == 0) {
            Cliente cliente = new Cliente();
            cliente.setUsuarioId(usuarioId);
            cliente.setNomeCliente(nome);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);
            clienteDAO.inserirCliente(cliente);
        } else {
            Empresa empresa = new Empresa();
            empresa.setUsuarioId(usuarioId);
            empresa.setNomeEmpresa(nome);
            empresa.setTelefone(telefone);
            empresa.setEndereco(endereco);
            empresaDAO.inserirEmpresa(empresa);
        }
    }

    public Usuario login(String email, String senha) throws Exception {
        String senhaHash = HashUtil.hashSenha(senha);
        Usuario usuario = usuarioDAO.obterUsuarioPorEmailSenha(email, senhaHash);
        if (usuario == null) {
            throw new Exception("Usuário e/ou senha incorretos!");
        }
        return usuario;
    }

    public Usuario obterUsuarioPorId(int usuarioId) throws SQLException {
        return usuarioDAO.obterUsuarioPorId(usuarioId);
    }
}