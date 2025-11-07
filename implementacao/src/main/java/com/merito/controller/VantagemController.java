package com.merito.controller;

import com.merito.dto.VantagemDTO;
import com.merito.entity.EmpresaParceira;
import com.merito.repository.EmpresaParceiraRepository;
import com.merito.service.VantagemService;
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
@RequestMapping("/api/vantagens")
@CrossOrigin(origins = "*")
public class VantagemController {
    
    @Autowired
    private VantagemService vantagemService;
    
    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;
    
    // Método auxiliar para obter a empresa autenticada
    private EmpresaParceira getEmpresaAutenticada() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        
        String email = authentication.getName();
        Optional<EmpresaParceira> empresaOpt = empresaParceiraRepository.findByEmail(email);
        
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Empresa não encontrada para o email: " + email);
        }
        
        return empresaOpt.get();
    }
    
    // POST /api/vantagens - Criar nova vantagem (apenas para empresas autenticadas)
    @PostMapping
    public ResponseEntity<?> criarVantagem(@Valid @RequestBody VantagemDTO vantagemDTO) {
        try {
            EmpresaParceira empresa = getEmpresaAutenticada();
            VantagemDTO vantagemCriada = vantagemService.criarVantagem(vantagemDTO, empresa.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(vantagemCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/vantagens - Listar todas as vantagens (público)
    @GetMapping
    public ResponseEntity<List<VantagemDTO>> listarTodasVantagens() {
        List<VantagemDTO> vantagens = vantagemService.listarTodasVantagens();
        return ResponseEntity.ok(vantagens);
    }
    
    // GET /api/vantagens/minhas - Listar vantagens da empresa autenticada
    @GetMapping("/minhas")
    public ResponseEntity<?> listarMinhasVantagens() {
        try {
            EmpresaParceira empresa = getEmpresaAutenticada();
            List<VantagemDTO> vantagens = vantagemService.listarVantagensPorEmpresa(empresa.getId());
            return ResponseEntity.ok(vantagens);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/vantagens/empresa/{empresaId} - Listar vantagens de uma empresa específica
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> listarVantagensPorEmpresa(@PathVariable Long empresaId) {
        try {
            List<VantagemDTO> vantagens = vantagemService.listarVantagensPorEmpresa(empresaId);
            return ResponseEntity.ok(vantagens);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // GET /api/vantagens/{id} - Buscar vantagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVantagemPorId(@PathVariable Long id) {
        Optional<VantagemDTO> vantagem = vantagemService.buscarVantagemPorId(id);
        if (vantagem.isPresent()) {
            return ResponseEntity.ok(vantagem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT /api/vantagens/{id} - Atualizar vantagem (apenas para empresas autenticadas)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVantagem(@PathVariable Long id, @Valid @RequestBody VantagemDTO vantagemDTO) {
        try {
            EmpresaParceira empresa = getEmpresaAutenticada();
            VantagemDTO vantagemAtualizada = vantagemService.atualizarVantagem(id, vantagemDTO, empresa.getId());
            return ResponseEntity.ok(vantagemAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // DELETE /api/vantagens/{id} - Deletar vantagem (apenas para empresas autenticadas)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarVantagem(@PathVariable Long id) {
        try {
            EmpresaParceira empresa = getEmpresaAutenticada();
            vantagemService.deletarVantagem(id, empresa.getId());
            return ResponseEntity.ok().body("Vantagem deletada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}

