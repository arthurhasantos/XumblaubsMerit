package com.merito.controller;

import com.merito.dto.TransferenciaMoedasDTO;
import com.merito.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;
    
    // GET /api/professores/me - Buscar dados do próprio professor autenticado
    @GetMapping("/me")
    public ResponseEntity<?> buscarMeusDados() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuário não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            String email = authentication.getName();
            var professor = professorService.buscarProfessorPorEmail(email);
            
            if (professor.isPresent()) {
                return ResponseEntity.ok(professor.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // POST /api/professores/transferir-moedas - Transferir moedas para um aluno
    @PostMapping("/transferir-moedas")
    public ResponseEntity<?> transferirMoedas(@Valid @RequestBody TransferenciaMoedasDTO transferenciaDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuário não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            String professorEmail = authentication.getName();
            professorService.transferirMoedas(professorEmail, transferenciaDTO);
            
            // Retornar JSON em vez de string simples
            Map<String, String> response = new HashMap<>();
            response.put("message", "Moedas transferidas com sucesso!");
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

