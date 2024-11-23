package br.com.fiap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.BO.EmpresaBO;
import br.com.fiap.BO.ServicoBO;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Servico;
import br.com.fiap.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    private EmpresaBO empresaBO;
    private ServicoBO servicoBO;

    public EmpresaController() throws Exception {
        empresaBO = new EmpresaBO();
        servicoBO = new ServicoBO();
    }

    @GetMapping
    public ResponseEntity<?> getEmpresa(HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            Empresa empresa = empresaBO.obterEmpresa(usuarioId);

            return ResponseEntity.ok().body(empresa);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateEmpresa(@RequestBody Empresa empresa, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            empresaBO.atualizarEmpresa(empresa, usuarioId);
            return ResponseEntity.ok("{\"message\": \"Informações da empresa atualizadas com sucesso!\"}");
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @GetMapping("/services")
    public ResponseEntity<?> getEmpresaServices(HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            List<Servico> servicos = servicoBO.obterServicosDaEmpresa(usuarioId);
            return ResponseEntity.ok(servicos);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    // Métodos auxiliares

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