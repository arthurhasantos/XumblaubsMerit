package com.merito.service;

import com.merito.dto.ProfessorDTO;
import com.merito.dto.TransferenciaMoedasDTO;
import com.merito.entity.Aluno;
import com.merito.entity.Professor;
import com.merito.repository.AlunoRepository;
import com.merito.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private TransacaoMoedaService transacaoMoedaService;
    
    // Buscar professor por email
    public Optional<ProfessorDTO> buscarProfessorPorEmail(String email) {
        return professorRepository.findByEmail(email)
                .map(this::converterParaDTO);
    }
    
    // Transferir moedas do professor para o aluno
    public void transferirMoedas(String professorEmail, TransferenciaMoedasDTO transferenciaDTO) {
        // Validar DTO
        if (transferenciaDTO.getAlunoId() == null) {
            throw new RuntimeException("ID do aluno é obrigatório");
        }
        
        // Buscar professor
        Professor professor = professorRepository.findByEmail(professorEmail)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com email: " + professorEmail));
        
        // Buscar aluno
        Long alunoId = transferenciaDTO.getAlunoId();
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));
        
        // Validar se o professor tem saldo suficiente
        if (professor.getSaldoMoedas() < transferenciaDTO.getQuantidade()) {
            throw new RuntimeException("Saldo insuficiente. Você possui " + professor.getSaldoMoedas() + 
                                     " moedas, mas precisa de " + transferenciaDTO.getQuantidade() + " moedas.");
        }
        
        // Validar se o aluno pertence à mesma instituição do professor
        Long professorInstituicaoId = professor.getInstituicao().getId();
        Long alunoInstituicaoId = aluno.getInstituicao().getId();
        if (professorInstituicaoId == null || alunoInstituicaoId == null || !professorInstituicaoId.equals(alunoInstituicaoId)) {
            throw new RuntimeException("O aluno não pertence à mesma instituição do professor.");
        }
        
        // Validar quantidade positiva
        if (transferenciaDTO.getQuantidade() <= 0) {
            throw new RuntimeException("A quantidade de moedas deve ser maior que zero.");
        }
        
        // Transferir moedas: subtrair do professor e adicionar ao aluno
        Double novoSaldoProfessor = professor.getSaldoMoedas() - transferenciaDTO.getQuantidade();
        Double novoSaldoAluno = aluno.getSaldoMoedas() + transferenciaDTO.getQuantidade();
        
        professor.setSaldoMoedas(novoSaldoProfessor);
        aluno.setSaldoMoedas(novoSaldoAluno);
        
        // Salvar alterações
        professorRepository.save(professor);
        alunoRepository.save(aluno);
        
        // Registrar transação no histórico
        transacaoMoedaService.criarTransacao(
            professor, 
            aluno, 
            transferenciaDTO.getQuantidade(), 
            transferenciaDTO.getMotivo()
        );
    }
    
    // Método auxiliar para converter entidade para DTO
    private ProfessorDTO converterParaDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        dto.setCpf(professor.getCpf());
        dto.setDepartamento(professor.getDepartamento());
        dto.setSaldoMoedas(professor.getSaldoMoedas());
        dto.setInstituicaoId(professor.getInstituicao().getId());
        dto.setInstituicaoNome(professor.getInstituicao().getNome());
        return dto;
    }
}

