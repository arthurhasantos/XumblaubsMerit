package com.merito.controller;

import com.merito.dto.EmpresaParceiraDTO;
import com.merito.service.EmpresaParceiraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaParceiraController {
    
    @Autowired
    private EmpresaParceiraService empresaParceiraService;
    
    // POST /api/empresas - Criar nova empresa parceira
    @PostMapping
    public ResponseEntity<?> criarEmpresaParceira(@Valid @RequestBody EmpresaParceiraDTO empresaDTO) {
        try {
            EmpresaParceiraDTO empresaCriada = empresaParceiraService.criarEmpresaParceira(empresaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresaCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/empresas - Listar todas as empresas parceiras
    @GetMapping
    public ResponseEntity<List<EmpresaParceiraDTO>> listarTodasEmpresas() {
        List<EmpresaParceiraDTO> empresas = empresaParceiraService.listarTodasEmpresas();
        return ResponseEntity.ok(empresas);
    }
    
    // GET /api/empresas/{id} - Buscar empresa por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaPorId(@PathVariable Long id) {
        Optional<EmpresaParceiraDTO> empresa = empresaParceiraService.buscarEmpresaPorId(id);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/empresas/cnpj/{cnpj} - Buscar empresa por CNPJ
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<?> buscarEmpresaPorCnpj(@PathVariable String cnpj) {
        Optional<EmpresaParceiraDTO> empresa = empresaParceiraService.buscarEmpresaPorCnpj(cnpj);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/empresas/email/{email} - Buscar empresa por email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarEmpresaPorEmail(@PathVariable String email) {
        Optional<EmpresaParceiraDTO> empresa = empresaParceiraService.buscarEmpresaPorEmail(email);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/empresas/nome/{nome} - Buscar empresa por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> buscarEmpresaPorNome(@PathVariable String nome) {
        Optional<EmpresaParceiraDTO> empresa = empresaParceiraService.buscarEmpresaPorNome(nome);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT /api/empresas/{id} - Atualizar empresa
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaParceiraDTO empresaDTO) {
        try {
            EmpresaParceiraDTO empresaAtualizada = empresaParceiraService.atualizarEmpresa(id, empresaDTO);
            return ResponseEntity.ok(empresaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // DELETE /api/empresas/{id} - Deletar empresa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Long id) {
        try {
            empresaParceiraService.deletarEmpresa(id);
            return ResponseEntity.ok().body("Empresa deletada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
