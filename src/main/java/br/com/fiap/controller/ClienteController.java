package br.com.fiap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.BO.ClienteBO;
import br.com.fiap.model.Cliente;
import br.com.fiap.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private ClienteBO clienteBO;

    public ClienteController() throws Exception {
        clienteBO = new ClienteBO();
    }

    @GetMapping
    public ResponseEntity<?> getCliente(HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 0) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            Cliente cliente = clienteBO.obterCliente(usuarioId);

            return ResponseEntity.ok().body(cliente);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 0) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());

            clienteBO.atualizarCliente(cliente, usuarioId);
            return ResponseEntity.ok("{\"message\": \"Informações do cliente atualizadas com sucesso!\"}");
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