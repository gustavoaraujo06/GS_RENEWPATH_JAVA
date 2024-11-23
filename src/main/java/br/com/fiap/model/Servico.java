package br.com.fiap.model;

public class Servico {
    private int servicoId;
    private int empresaId;
    private String nomeServico;
    private String descricao;
    private double preco;

    
    
    public Servico() {
		super();
	}
    
    
	public Servico(int servicoId, int empresaId, String nomeServico, String descricao, double preco) {
		super();
		this.servicoId = servicoId;
		this.empresaId = empresaId;
		this.nomeServico = nomeServico;
		this.descricao = descricao;
		this.preco = preco;
	}


	public int getServicoId() {
        return servicoId;
    }
    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }
    public int getEmpresaId() {
        return empresaId;
    }
    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }
    public String getNomeServico() {
        return nomeServico;
    }
    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
}