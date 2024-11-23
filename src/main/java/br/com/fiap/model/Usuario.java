package br.com.fiap.model;

public class Usuario {
    private int usuarioId;
    private String email;
    private String senha;
    private int usuarioRole;

    
    
    public Usuario() {
		super();
	}
    
	public Usuario(int usuarioId, String email, String senha, int usuarioRole) {
		super();
		this.usuarioId = usuarioId;
		this.email = email;
		this.senha = senha;
		this.usuarioRole = usuarioRole;
	}

	public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public int getUsuarioRole() {
        return usuarioRole;
    }
    public void setUsuarioRole(int usuarioRole) {
        this.usuarioRole = usuarioRole;
    }
}