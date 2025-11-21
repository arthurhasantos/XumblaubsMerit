package com.merito.controller;

import com.merito.dto.TransacaoMoedaDTO;
import com.merito.service.TransacaoMoedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@CrossOrigin(origins = "*")
public class TransacaoMoedaController {
    
    @Autowired
    private TransacaoMoedaService transacaoService;
    
    // GET /api/transacoes/professor - Buscar histórico do professor autenticado
    @GetMapping("/professor")
    public ResponseEntity<?> buscarHistoricoProfessor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
            
            String professorEmail = authentication.getName();
            List<TransacaoMoedaDTO> historico = transacaoService.buscarHistoricoProfessor(professorEmail);
            
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
        }
    }
    
    // GET /api/transacoes/aluno - Buscar histórico do aluno autenticado
    @GetMapping("/aluno")
    public ResponseEntity<?> buscarHistoricoAluno() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
            
            String alunoEmail = authentication.getName();
            List<TransacaoMoedaDTO> historico = transacaoService.buscarHistoricoAluno(alunoEmail);
            
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
        }
    }
}

