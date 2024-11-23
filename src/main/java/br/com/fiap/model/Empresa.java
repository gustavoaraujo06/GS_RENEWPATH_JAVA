package br.com.fiap.model;

public class Empresa {
    private int empresaId;
    private int usuarioId;
    private String nomeEmpresa;
    private String endereco;
    private String telefone;

    
    
    public Empresa() {
		super();
	}
    
    
	public Empresa(int empresaId, int usuarioId, String nomeEmpresa, String endereco, String telefone) {
		super();
		this.empresaId = empresaId;
		this.usuarioId = usuarioId;
		this.nomeEmpresa = nomeEmpresa;
		this.endereco = endereco;
		this.telefone = telefone;
	}


	public int getEmpresaId() {
        return empresaId;
    }
    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }
    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
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