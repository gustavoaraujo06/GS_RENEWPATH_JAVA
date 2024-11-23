package br.com.fiap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.fiap.BO.SolicitacaoBO;
import br.com.fiap.model.Cliente;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Servico;
import br.com.fiap.model.Solicitacao;
import br.com.fiap.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/requests")
public class SolicitacaoController {

    private SolicitacaoBO solicitacaoBO;

    public SolicitacaoController() throws Exception {
        solicitacaoBO = new SolicitacaoBO();
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 0) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            if (!requestBody.containsKey("servico_id")) {
                return ResponseEntity.badRequest().body("{\"message\": \"servico_id é necessário!\"}");
            }
            int servicoId = (int) requestBody.get("servico_id");

            solicitacaoBO.criarSolicitacao(servicoId, usuarioId);
            return ResponseEntity.status(201).body("{\"message\": \"Solicitação criada com sucesso! Aguarde a empresa entrar em contato\"}");
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getRequests(HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int usuarioId = Integer.parseInt(claims.getSubject());
            int role = (int) claims.get("role");

            List<Solicitacao> solicitacoes = solicitacaoBO.obterSolicitacoes(usuarioId, role);

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Solicitacao solicitacao : solicitacoes) {
                Map<String, Object> solicitacaoMap = new HashMap<>();
                solicitacaoMap.put("solicitacao_id", solicitacao.getSolicitacaoId());
                solicitacaoMap.put("data_solicitacao", solicitacao.getDataSolicitacao().toString());
                solicitacaoMap.put("status", solicitacao.getStatus());

                Servico servico = solicitacaoBO.obterServicoPorId(solicitacao.getServicoId());
                solicitacaoMap.put("nome_servico", servico.getNomeServico());

                if (role == 0) {
                    Empresa empresa = solicitacaoBO.obterEmpresaPorServicoId(servico.getServicoId());
                    solicitacaoMap.put("nome_empresa", empresa.getNomeEmpresa());
                } else if (role == 1) {
                    Cliente cliente = solicitacaoBO.obterClientePorId(solicitacao.getClienteId());
                    solicitacaoMap.put("cliente_id", cliente.getClienteId());
                    solicitacaoMap.put("nome_cliente", cliente.getNomeCliente());
                }

                responseList.add(solicitacaoMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @PutMapping("/{solicitacao_id}")
    public ResponseEntity<?> updateRequest(@PathVariable("solicitacao_id") int solicitacaoId, @RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            if (!requestBody.containsKey("status")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Status é necessário!\"}");
            }
            String novoStatus = (String) requestBody.get("status");

            solicitacaoBO.atualizarSolicitacao(solicitacaoId, novoStatus, usuarioId);
            return ResponseEntity.ok("{\"message\": \"Solicitação atualizada com sucesso!\"}");
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @GetMapping(value = "/export/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exportRequests(HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int usuarioId = Integer.parseInt(claims.getSubject());
            int role = (int) claims.get("role");

            List<Solicitacao> solicitacoes = solicitacaoBO.exportarSolicitacoes(usuarioId, role);

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Solicitacao solicitacao : solicitacoes) {
                Map<String, Object> solicitacaoMap = new HashMap<>();
                solicitacaoMap.put("solicitacao_id", solicitacao.getSolicitacaoId());
                solicitacaoMap.put("data_solicitacao", solicitacao.getDataSolicitacao().toString());
                solicitacaoMap.put("status", solicitacao.getStatus());

                Servico servico = solicitacaoBO.obterServicoPorId(solicitacao.getServicoId());
                solicitacaoMap.put("nome_servico", servico.getNomeServico());

                if (role == 0) {
                    Empresa empresa = solicitacaoBO.obterEmpresaPorServicoId(servico.getServicoId());
                    solicitacaoMap.put("nome_empresa", empresa.getNomeEmpresa());
                } else if (role == 1) {
                    Cliente cliente = solicitacaoBO.obterClientePorId(solicitacao.getClienteId());
                    solicitacaoMap.put("nome_cliente", cliente.getNomeCliente());
                }

                responseList.add(solicitacaoMap);
            }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(responseList);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;filename=solicitacoes.json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }


    private Claims validarToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new Exception("Token está faltando!");
        }
        return JwtUtil.parseToken(token);
    }

    private ResponseEntity<String> tratarExcecao(Exception e) {
        if (e.getMessage().contains("Não autorizado")) {
            return ResponseEntity.status(403).body("{\"message\": \"" + e.getMessage() + "\"}");
        } else if (e.getMessage().contains("não encontrado")) {
            return ResponseEntity.status(404).body("{\"message\": \"" + e.getMessage() + "\"}");
        } else if (e.getMessage().contains("inválido")) {
            return ResponseEntity.status(400).body("{\"message\": \"" + e.getMessage() + "\"}");
        } else {
            return ResponseEntity.status(500).body("{\"message\": \"Erro interno!\"}");
        }
    }
}
