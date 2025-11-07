package com.merito.service;

import com.merito.dto.ResgateVantagemDTO;
import com.merito.entity.Aluno;
import com.merito.entity.ResgateVantagem;
import com.merito.entity.Vantagem;
import com.merito.repository.AlunoRepository;
import com.merito.repository.ResgateVantagemRepository;
import com.merito.repository.VantagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResgateVantagemService {
    
    @Autowired
    private ResgateVantagemRepository resgateRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private VantagemRepository vantagemRepository;
    
    // Criar resgate de vantagem
    public ResgateVantagemDTO resgatarVantagem(Long vantagemId, Long alunoId) {
        // Buscar aluno
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));
        
        // Buscar vantagem
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + vantagemId));
        
        // Validar saldo suficiente
        if (aluno.getSaldoMoedas() < vantagem.getCustoEmMoedas()) {
            throw new RuntimeException("Saldo insuficiente. Você possui " + aluno.getSaldoMoedas() + 
                                     " moedas, mas precisa de " + vantagem.getCustoEmMoedas() + " moedas.");
        }
        
        // Gerar código de cupom único
        String codigoCupom = gerarCodigoCupomUnico();
        
        // Criar resgate
        ResgateVantagem resgate = new ResgateVantagem();
        resgate.setDataResgate(LocalDateTime.now());
        resgate.setCodigoCupom(codigoCupom);
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);
        
        // Subtrair moedas do aluno
        Double novoSaldo = aluno.getSaldoMoedas() - vantagem.getCustoEmMoedas();
        aluno.setSaldoMoedas(novoSaldo);
        alunoRepository.save(aluno);
        
        // Salvar resgate
        ResgateVantagem resgateSalvo = resgateRepository.save(resgate);
        
        // Retornar DTO
        return converterParaDTO(resgateSalvo);
    }
    
    // Gerar código de cupom único
    private String gerarCodigoCupomUnico() {
        String codigo;
        do {
            // Gerar código no formato: CUPOM-XXXX-XXXX-XXXX
            codigo = "CUPOM-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase() + 
                    "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase() + 
                    "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        } while (resgateRepository.existsByCodigoCupom(codigo));
        
        return codigo;
    }
    
    // Listar resgates de um aluno
    public List<ResgateVantagemDTO> listarResgatesPorAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));
        
        return resgateRepository.findByAlunoOrderByDataResgateDesc(aluno).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar resgate por ID
    public Optional<ResgateVantagemDTO> buscarResgatePorId(Long id) {
        return resgateRepository.findById(id)
                .map(this::converterParaDTO);
    }
    
    // Buscar resgate por código de cupom
    public Optional<ResgateVantagemDTO> buscarResgatePorCodigoCupom(String codigoCupom) {
        return resgateRepository.findByCodigoCupom(codigoCupom)
                .map(this::converterParaDTO);
    }
    
    // Método auxiliar para converter entidade para DTO
    private ResgateVantagemDTO converterParaDTO(ResgateVantagem resgate) {
        ResgateVantagemDTO dto = new ResgateVantagemDTO();
        dto.setId(resgate.getId());
        dto.setDataResgate(resgate.getDataResgate());
        dto.setCodigoCupom(resgate.getCodigoCupom());
        dto.setAlunoId(resgate.getAluno().getId());
        dto.setAlunoNome(resgate.getAluno().getNome());
        dto.setVantagemId(resgate.getVantagem().getId());
        dto.setVantagemNome(resgate.getVantagem().getNome());
        dto.setVantagemFotoUrl(resgate.getVantagem().getFotoUrl());
        dto.setCustoEmMoedas(resgate.getVantagem().getCustoEmMoedas());
        return dto;
    }
}

