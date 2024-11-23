package br.com.fiap.model;

import java.util.Date;

public class Solicitacao {
    private int solicitacaoId;
    private int clienteId;
    private int servicoId;
    private Date dataSolicitacao;
    private String status;

    
    
    public Solicitacao() {
		super();
	}
    
    
	public Solicitacao(int solicitacaoId, int clienteId, int servicoId, Date dataSolicitacao, String status) {
		super();
		this.solicitacaoId = solicitacaoId;
		this.clienteId = clienteId;
		this.servicoId = servicoId;
		this.dataSolicitacao = dataSolicitacao;
		this.status = status;
	}


	public int getSolicitacaoId() {
        return solicitacaoId;
    }
    public void setSolicitacaoId(int solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }
    public int getClienteId() {
        return clienteId;
    }
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    public int getServicoId() {
        return servicoId;
    }
    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }
    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }
    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}