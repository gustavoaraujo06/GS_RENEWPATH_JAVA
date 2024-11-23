package br.com.fiap.model;

public class Cliente {
    private int clienteId;
    private int usuarioId;
    private String nomeCliente;
    private String endereco;
    private String telefone;
    
    
    public Cliente() {
		super();
	}
    
	public Cliente(int clienteId, int usuarioId, String nomeCliente, String endereco, String telefone) {
		super();
		this.clienteId = clienteId;
		this.usuarioId = usuarioId;
		this.nomeCliente = nomeCliente;
		this.endereco = endereco;
		this.telefone = telefone;
	}

	public int getClienteId() {
        return clienteId;
    }
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}