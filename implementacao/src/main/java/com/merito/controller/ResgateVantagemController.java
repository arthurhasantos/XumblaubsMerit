package com.merito.controller;

import com.merito.dto.ResgateVantagemDTO;
import com.merito.entity.Aluno;
import com.merito.repository.AlunoRepository;
import com.merito.service.ResgateVantagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resgates")
@CrossOrigin(origins = "*")
public class ResgateVantagemController {
    
    @Autowired
    private ResgateVantagemService resgateService;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    // Método auxiliar para obter o aluno autenticado
    private Aluno getAlunoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        
        String email = authentication.getName();
        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(email);
        
        if (alunoOpt.isEmpty()) {
            throw new RuntimeException("Aluno não encontrado para o email: " + email);
        }
        
        return alunoOpt.get();
    }
    
    // POST /api/resgates - Resgatar vantagem (apenas para alunos autenticados)
    @PostMapping
    public ResponseEntity<?> resgatarVantagem(@RequestBody ResgateRequest request) {
        try {
            Aluno aluno = getAlunoAutenticado();
            ResgateVantagemDTO resgate = resgateService.resgatarVantagem(request.getVantagemId(), aluno.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(resgate);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/resgates/meus - Listar resgates do aluno autenticado
    @GetMapping("/meus")
    public ResponseEntity<?> listarMeusResgates() {
        try {
            Aluno aluno = getAlunoAutenticado();
            List<ResgateVantagemDTO> resgates = resgateService.listarResgatesPorAluno(aluno.getId());
            return ResponseEntity.ok(resgates);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/resgates/{id} - Buscar resgate por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarResgatePorId(@PathVariable Long id) {
        Optional<ResgateVantagemDTO> resgate = resgateService.buscarResgatePorId(id);
        if (resgate.isPresent()) {
            return ResponseEntity.ok(resgate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/resgates/cupom/{codigoCupom} - Buscar resgate por código de cupom
    @GetMapping("/cupom/{codigoCupom}")
    public ResponseEntity<?> buscarResgatePorCodigoCupom(@PathVariable String codigoCupom) {
        Optional<ResgateVantagemDTO> resgate = resgateService.buscarResgatePorCodigoCupom(codigoCupom);
        if (resgate.isPresent()) {
            return ResponseEntity.ok(resgate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Classe interna para request de resgate
    public static class ResgateRequest {
        private Long vantagemId;
        
        public Long getVantagemId() {
            return vantagemId;
        }
        
        public void setVantagemId(Long vantagemId) {
            this.vantagemId = vantagemId;
        }
    }
}

