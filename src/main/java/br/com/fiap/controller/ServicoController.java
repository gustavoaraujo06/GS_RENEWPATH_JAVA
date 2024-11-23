package br.com.fiap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.BO.ServicoBO;
import br.com.fiap.model.Servico;
import br.com.fiap.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/services")
public class ServicoController {

    private ServicoBO servicoBO;

    public ServicoController() throws Exception {
        servicoBO = new ServicoBO();
    }

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody Servico servico, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());
            servicoBO.adicionarServico(servico, usuarioId);
            return ResponseEntity.status(201).body("{\"message\": \"Serviço adicionado com sucesso!\"}");
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getServices(
            @RequestParam(required = false) String nome_servico,
            @RequestParam(required = false) Double min_preco,
            @RequestParam(required = false) Double max_preco,
            @RequestParam(required = false) String order_by,
            @RequestParam(required = false) Boolean descending,
            HttpServletRequest request) {
        try {
            validarToken(request);
            List<Servico> servicos = servicoBO.obterServicosComFiltros(nome_servico, min_preco, max_preco, order_by, descending);
            return ResponseEntity.ok(servicos);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @GetMapping("/{service_id}")
    public ResponseEntity<?> getServiceById(@PathVariable("service_id") int serviceId, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());
            Servico servico = servicoBO.obterServicoPorId(serviceId, usuarioId);
            return ResponseEntity.ok(servico);
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @PutMapping("/{service_id}")
    public ResponseEntity<?> updateService(@PathVariable("service_id") int serviceId, @RequestBody Servico servico, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());
            servico.setServicoId(serviceId);
            servicoBO.atualizarServico(servico, usuarioId);
            return ResponseEntity.ok("{\"message\": \"Serviço atualizado com sucesso!\"}");
        } catch (Exception e) {
            return tratarExcecao(e);
        }
    }

    @DeleteMapping("/{service_id}")
    public ResponseEntity<?> deleteService(@PathVariable("service_id") int serviceId, HttpServletRequest request) {
        try {
            Claims claims = validarToken(request);
            int role = (int) claims.get("role");
            if (role != 1) {
                return ResponseEntity.status(403).body("{\"message\": \"Não autorizado!\"}");
            }
            int usuarioId = Integer.parseInt(claims.getSubject());
            servicoBO.deletarServico(serviceId, usuarioId);
            return ResponseEntity.ok("{\"message\": \"Serviço deletado com sucesso!\"}");
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
