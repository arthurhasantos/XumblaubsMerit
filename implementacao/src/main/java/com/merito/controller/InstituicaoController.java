package com.merito.controller;

import com.merito.entity.Instituicao;
import com.merito.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instituicoes")
@CrossOrigin(origins = "*")
public class InstituicaoController {
    
    @Autowired
    private InstituicaoRepository instituicaoRepository;
    
    // GET /api/instituicoes - Listar todas as instituições
    @GetMapping
    public ResponseEntity<List<Instituicao>> listarTodasInstituicoes() {
        List<Instituicao> instituicoes = instituicaoRepository.findAll();
        return ResponseEntity.ok(instituicoes);
    }
    
    // GET /api/instituicoes/{id} - Buscar instituição por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInstituicaoPorId(@PathVariable Long id) {
        Optional<Instituicao> instituicao = instituicaoRepository.findById(id);
        if (instituicao.isPresent()) {
            return ResponseEntity.ok(instituicao.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/instituicoes/nome/{nome} - Buscar instituição por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> buscarInstituicaoPorNome(@PathVariable String nome) {
        Optional<Instituicao> instituicao = instituicaoRepository.findByNome(nome);
        if (instituicao.isPresent()) {
            return ResponseEntity.ok(instituicao.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
