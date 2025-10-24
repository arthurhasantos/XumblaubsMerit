package com.merito.controller;

import com.merito.dto.LoginRequest;
import com.merito.dto.LoginResponse;
import com.merito.entity.Usuario;
import com.merito.repository.UsuarioRepository;
import com.merito.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validação manual
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }
            if (loginRequest.getSenha() == null || loginRequest.getSenha().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }
            
            // Buscar usuário por email
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Credenciais inválidas");
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar se é ADMIN
            if (!"ADMIN".equals(usuario.getTipoUsuario())) {
                return ResponseEntity.badRequest().body("Acesso negado. Apenas administradores podem fazer login.");
            }
            
            // Verificar senha
            if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                return ResponseEntity.badRequest().body("Credenciais inválidas");
            }
            
            // Gerar token JWT
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getTipoUsuario());
            
            // Retornar resposta
            LoginResponse response = new LoginResponse(
                token,
                usuario.getTipoUsuario(),
                usuario.getEmail(),
                "Administrador" // Nome fixo para ADMIN
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Token inválido");
            }
            
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)) {
                String email = jwtUtil.getEmailFromToken(token);
                String tipoUsuario = jwtUtil.getTipoUsuarioFromToken(token);
                
                return ResponseEntity.ok(new LoginResponse(token, tipoUsuario, email, null));
            } else {
                return ResponseEntity.badRequest().body("Token inválido ou expirado");
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na validação: " + e.getMessage());
        }
    }
}
