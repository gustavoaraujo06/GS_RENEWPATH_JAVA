package br.com.fiap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.BO.UsuarioBO;
import br.com.fiap.model.Usuario;
import br.com.fiap.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UsuarioBO usuarioBO;

    public AuthController() throws Exception {
        usuarioBO = new UsuarioBO();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (registerRequest.getEmail() == null || registerRequest.getSenha() == null ||
                registerRequest.getNome() == null || registerRequest.getTelefone() == null ||
                registerRequest.getEndereco() == null || registerRequest.getRole() == null) {
                return ResponseEntity.badRequest().body("{\"message\": \"Email, Senha, Nome, Telefone, Endereco e Role são necessários!\"}");
            }
            Usuario usuario = new Usuario();
            usuario.setEmail(registerRequest.getEmail());
            usuario.setSenha(registerRequest.getSenha());
            usuario.setUsuarioRole(registerRequest.getRole());

            usuarioBO.registrarUsuario(usuario, registerRequest.getNome(), registerRequest.getTelefone(), registerRequest.getEndereco());
            return ResponseEntity.status(201).body("{\"message\": \"Usuário cadastrado com sucesso!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getSenha() == null) {
                return ResponseEntity.badRequest().body("{\"message\": \"Email e Senha são necessários!\"}");
            }
            Usuario usuarioLogado = usuarioBO.login(loginRequest.getEmail(), loginRequest.getSenha());
            String token = JwtUtil.generateToken("" + usuarioLogado.getUsuarioId(), usuarioLogado.getUsuarioRole());
            return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> decode(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null) {
                return ResponseEntity.status(401).body("{\"message\": \"Token está faltando!\"}");
            }
            Claims claims = JwtUtil.parseToken(token);
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
}


class RegisterRequest {
    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private String endereco;
    private Integer role;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}

    
}
class LoginRequest {
    private String email;
    private String senha;
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

    
}
