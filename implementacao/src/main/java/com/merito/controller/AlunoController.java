package com.merito.controller;

import com.merito.dto.AlunoDTO;
import com.merito.dto.AlunoUpdateDTO;
import com.merito.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;
    
    // POST /api/alunos - Criar novo aluno
    @PostMapping
    public ResponseEntity<?> criarAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO alunoCriado = alunoService.criarAluno(alunoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/alunos - Listar todos os alunos
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodosAlunos() {
        List<AlunoDTO> alunos = alunoService.listarTodosAlunos();
        return ResponseEntity.ok(alunos);
    }
    
    // GET /api/alunos/{id} - Buscar aluno por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAlunoPorId(@PathVariable Long id) {
        Optional<AlunoDTO> aluno = alunoService.buscarAlunoPorId(id);
        if (aluno.isPresent()) {
            return ResponseEntity.ok(aluno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/alunos/cpf/{cpf} - Buscar aluno por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> buscarAlunoPorCpf(@PathVariable String cpf) {
        Optional<AlunoDTO> aluno = alunoService.buscarAlunoPorCpf(cpf);
        if (aluno.isPresent()) {
            return ResponseEntity.ok(aluno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/alunos/email/{email} - Buscar aluno por email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarAlunoPorEmail(@PathVariable String email) {
        Optional<AlunoDTO> aluno = alunoService.buscarAlunoPorEmail(email);
        if (aluno.isPresent()) {
            return ResponseEntity.ok(aluno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/alunos/me - Buscar dados do próprio aluno autenticado
    @GetMapping("/me")
    public ResponseEntity<?> buscarMeusDados() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
            
            String email = authentication.getName();
            Optional<AlunoDTO> aluno = alunoService.buscarAlunoPorEmail(email);
            
            if (aluno.isPresent()) {
                return ResponseEntity.ok(aluno.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/alunos/instituicao/{instituicaoId} - Buscar alunos por instituição
    @GetMapping("/instituicao/{instituicaoId}")
    public ResponseEntity<?> buscarAlunosPorInstituicao(@PathVariable Long instituicaoId) {
        try {
            List<AlunoDTO> alunos = alunoService.buscarAlunosPorInstituicao(instituicaoId);
            return ResponseEntity.ok(alunos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/alunos/curso/{curso} - Buscar alunos por curso
    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<AlunoDTO>> buscarAlunosPorCurso(@PathVariable String curso) {
        List<AlunoDTO> alunos = alunoService.buscarAlunosPorCurso(curso);
        return ResponseEntity.ok(alunos);
    }
    
    // PUT /api/alunos/{id} - Atualizar aluno
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, @Valid @RequestBody AlunoUpdateDTO alunoUpdateDTO) {
        try {
            AlunoDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoUpdateDTO);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // DELETE /api/alunos/{id} - Deletar aluno
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAluno(@PathVariable Long id) {
        try {
            alunoService.deletarAluno(id);
            return ResponseEntity.ok().body("Aluno deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
