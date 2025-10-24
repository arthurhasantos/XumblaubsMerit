package com.merito.service;

import com.merito.dto.AlunoDTO;
import com.merito.entity.Aluno;
import com.merito.entity.Instituicao;
import com.merito.repository.AlunoRepository;
import com.merito.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlunoService {
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private InstituicaoRepository instituicaoRepository;
    
    // Create
    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        // Validar se CPF já existe
        if (alunoRepository.existsByCpf(alunoDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + alunoDTO.getCpf());
        }
        
        // Validar se email já existe
        if (alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + alunoDTO.getEmail());
        }
        
        // Buscar instituição
        Instituicao instituicao = instituicaoRepository.findById(alunoDTO.getInstituicaoId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada com ID: " + alunoDTO.getInstituicaoId()));
        
        // Criar entidade
        Aluno aluno = new Aluno();
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setSenha(alunoDTO.getSenha()); // TODO: Implementar hash da senha
        aluno.setTipoUsuario("ALUNO");
        aluno.setNome(alunoDTO.getNome());
        aluno.setCpf(alunoDTO.getCpf());
        aluno.setRg(alunoDTO.getRg());
        aluno.setEndereco(alunoDTO.getEndereco());
        aluno.setCurso(alunoDTO.getCurso());
        aluno.setSaldoMoedas(alunoDTO.getSaldoMoedas() != null ? alunoDTO.getSaldoMoedas() : 0.0);
        aluno.setInstituicao(instituicao);
        
        // Salvar
        Aluno alunoSalvo = alunoRepository.save(aluno);
        
        // Retornar DTO
        return converterParaDTO(alunoSalvo);
    }
    
    // Read
    public List<AlunoDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<AlunoDTO> buscarAlunoPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO);
    }
    
    public Optional<AlunoDTO> buscarAlunoPorCpf(String cpf) {
        return alunoRepository.findByCpf(cpf)
                .map(this::converterParaDTO);
    }
    
    public Optional<AlunoDTO> buscarAlunoPorEmail(String email) {
        return alunoRepository.findByEmail(email)
                .map(this::converterParaDTO);
    }
    
    public List<AlunoDTO> buscarAlunosPorInstituicao(Long instituicaoId) {
        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada com ID: " + instituicaoId));
        
        return alunoRepository.findByInstituicao(instituicao).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public List<AlunoDTO> buscarAlunosPorCurso(String curso) {
        return alunoRepository.findByCurso(curso).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Update
    public AlunoDTO atualizarAluno(Long id, AlunoDTO alunoDTO) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
        
        // Validar se CPF já existe em outro aluno
        if (!aluno.getCpf().equals(alunoDTO.getCpf()) && alunoRepository.existsByCpf(alunoDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + alunoDTO.getCpf());
        }
        
        // Validar se email já existe em outro aluno
        if (!aluno.getEmail().equals(alunoDTO.getEmail()) && alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + alunoDTO.getEmail());
        }
        
        // Buscar instituição se mudou
        if (!aluno.getInstituicao().getId().equals(alunoDTO.getInstituicaoId())) {
            Instituicao instituicao = instituicaoRepository.findById(alunoDTO.getInstituicaoId())
                    .orElseThrow(() -> new RuntimeException("Instituição não encontrada com ID: " + alunoDTO.getInstituicaoId()));
            aluno.setInstituicao(instituicao);
        }
        
        // Atualizar campos
        aluno.setNome(alunoDTO.getNome());
        aluno.setEmail(alunoDTO.getEmail());
        if (alunoDTO.getSenha() != null && !alunoDTO.getSenha().isEmpty()) {
            aluno.setSenha(alunoDTO.getSenha()); // TODO: Implementar hash da senha
        }
        aluno.setCpf(alunoDTO.getCpf());
        aluno.setRg(alunoDTO.getRg());
        aluno.setEndereco(alunoDTO.getEndereco());
        aluno.setCurso(alunoDTO.getCurso());
        if (alunoDTO.getSaldoMoedas() != null) {
            aluno.setSaldoMoedas(alunoDTO.getSaldoMoedas());
        }
        
        // Salvar
        Aluno alunoAtualizado = alunoRepository.save(aluno);
        
        // Retornar DTO
        return converterParaDTO(alunoAtualizado);
    }
    
    // Delete
    public void deletarAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
    
    // Método auxiliar para converter entidade para DTO
    private AlunoDTO converterParaDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setCpf(aluno.getCpf());
        dto.setRg(aluno.getRg());
        dto.setEndereco(aluno.getEndereco());
        dto.setCurso(aluno.getCurso());
        dto.setSaldoMoedas(aluno.getSaldoMoedas());
        dto.setInstituicaoId(aluno.getInstituicao().getId());
        return dto;
    }
}
