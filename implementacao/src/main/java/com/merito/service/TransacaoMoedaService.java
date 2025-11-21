package com.merito.service;

import com.merito.dto.TransacaoMoedaDTO;
import com.merito.entity.Aluno;
import com.merito.entity.Professor;
import com.merito.entity.TransacaoMoeda;
import com.merito.repository.AlunoRepository;
import com.merito.repository.ProfessorRepository;
import com.merito.repository.TransacaoMoedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransacaoMoedaService {
    
    @Autowired
    private TransacaoMoedaRepository transacaoRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    // Buscar histórico do professor (apenas ENVIADAS e RECEBIDAS do sistema, sem aluno)
    public List<TransacaoMoedaDTO> buscarHistoricoProfessor(String professorEmail) {
        Professor professor = professorRepository.findByEmail(professorEmail)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com email: " + professorEmail));
        
        // Buscar todas as transações do professor
        List<TransacaoMoeda> todasTransacoes = transacaoRepository.findByProfessorOrderByDataTransacaoDesc(professor);
        
        // Filtrar: apenas ENVIADAS ou RECEBIDAS do sistema (sem aluno)
        return todasTransacoes.stream()
                .filter(t -> t.getTipo().equals("ENVIADA") || (t.getTipo().equals("RECEBIDA") && t.getAluno() == null))
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar histórico do aluno (apenas recebidas)
    public List<TransacaoMoedaDTO> buscarHistoricoAluno(String alunoEmail) {
        Aluno aluno = alunoRepository.findByEmail(alunoEmail)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com email: " + alunoEmail));
        
        List<TransacaoMoeda> transacoes = transacaoRepository.findByAlunoAndTipoOrderByDataTransacaoDesc(aluno, "RECEBIDA");
        return transacoes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Criar transação (usado pelo ProfessorService)
    // Cria duas transações: uma ENVIADA (para o professor) e uma RECEBIDA (para o aluno)
    public void criarTransacao(Professor professor, Aluno aluno, Double quantidade, String motivo) {
        java.time.LocalDateTime agora = java.time.LocalDateTime.now();
        
        // Transação do ponto de vista do professor (ENVIADA)
        TransacaoMoeda transacaoProfessor = new TransacaoMoeda();
        transacaoProfessor.setDataTransacao(agora);
        transacaoProfessor.setQuantidade(quantidade);
        transacaoProfessor.setTipo("ENVIADA");
        transacaoProfessor.setMotivo(motivo);
        transacaoProfessor.setProfessor(professor);
        transacaoProfessor.setAluno(aluno);
        transacaoRepository.save(transacaoProfessor);
        
        // Transação do ponto de vista do aluno (RECEBIDA)
        TransacaoMoeda transacaoAluno = new TransacaoMoeda();
        transacaoAluno.setDataTransacao(agora);
        transacaoAluno.setQuantidade(quantidade);
        transacaoAluno.setTipo("RECEBIDA");
        transacaoAluno.setMotivo(motivo);
        transacaoAluno.setProfessor(professor);
        transacaoAluno.setAluno(aluno);
        transacaoRepository.save(transacaoAluno);
    }
    
    // Criar transação de crédito inicial do sistema para professor
    public void criarCreditoInicial(Professor professor) {
        TransacaoMoeda transacao = new TransacaoMoeda();
        transacao.setDataTransacao(java.time.LocalDateTime.now());
        transacao.setQuantidade(1000.0);
        transacao.setTipo("RECEBIDA"); // Professor recebe do sistema
        transacao.setMotivo("Crédito inicial do sistema");
        transacao.setProfessor(professor);
        transacao.setAluno(null); // Não há aluno envolvido
        transacaoRepository.save(transacao);
    }
    
    // Método auxiliar para converter entidade para DTO
    private TransacaoMoedaDTO converterParaDTO(TransacaoMoeda transacao) {
        TransacaoMoedaDTO dto = new TransacaoMoedaDTO();
        dto.setId(transacao.getId());
        dto.setDataTransacao(transacao.getDataTransacao());
        dto.setQuantidade(transacao.getQuantidade());
        dto.setTipo(transacao.getTipo());
        dto.setMotivo(transacao.getMotivo());
        dto.setProfessorId(transacao.getProfessor().getId());
        dto.setProfessorNome(transacao.getProfessor().getNome());
        if (transacao.getAluno() != null) {
            dto.setAlunoId(transacao.getAluno().getId());
            dto.setAlunoNome(transacao.getAluno().getNome());
        } else {
            dto.setAlunoId(null);
            dto.setAlunoNome("Sistema");
        }
        return dto;
    }
}

