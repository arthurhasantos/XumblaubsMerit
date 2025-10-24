package com.merito.service;

import com.merito.dto.EmpresaParceiraDTO;
import com.merito.entity.EmpresaParceira;
import com.merito.repository.EmpresaParceiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresaParceiraService {
    
    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;
    
    // Create
    public EmpresaParceiraDTO criarEmpresaParceira(EmpresaParceiraDTO empresaDTO) {
        // Validar se CNPJ já existe
        if (empresaParceiraRepository.existsByCnpj(empresaDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado: " + empresaDTO.getCnpj());
        }
        
        // Validar se email já existe
        if (empresaParceiraRepository.existsByEmail(empresaDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + empresaDTO.getEmail());
        }
        
        // Criar entidade
        EmpresaParceira empresa = new EmpresaParceira();
        empresa.setEmail(empresaDTO.getEmail());
        empresa.setSenha(empresaDTO.getSenha()); // TODO: Implementar hash da senha
        empresa.setTipoUsuario("EMPRESA_PARCEIRA");
        empresa.setNome(empresaDTO.getNome());
        empresa.setCnpj(empresaDTO.getCnpj());
        empresa.setEmailContato(empresaDTO.getEmailContato());
        
        // Salvar
        EmpresaParceira empresaSalva = empresaParceiraRepository.save(empresa);
        
        // Retornar DTO
        return converterParaDTO(empresaSalva);
    }
    
    // Read
    public List<EmpresaParceiraDTO> listarTodasEmpresas() {
        return empresaParceiraRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EmpresaParceiraDTO> buscarEmpresaPorId(Long id) {
        return empresaParceiraRepository.findById(id)
                .map(this::converterParaDTO);
    }
    
    public Optional<EmpresaParceiraDTO> buscarEmpresaPorCnpj(String cnpj) {
        return empresaParceiraRepository.findByCnpj(cnpj)
                .map(this::converterParaDTO);
    }
    
    public Optional<EmpresaParceiraDTO> buscarEmpresaPorEmail(String email) {
        return empresaParceiraRepository.findByEmail(email)
                .map(this::converterParaDTO);
    }
    
    public Optional<EmpresaParceiraDTO> buscarEmpresaPorNome(String nome) {
        return empresaParceiraRepository.findByNome(nome)
                .map(this::converterParaDTO);
    }
    
    // Update
    public EmpresaParceiraDTO atualizarEmpresa(Long id, EmpresaParceiraDTO empresaDTO) {
        EmpresaParceira empresa = empresaParceiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + id));
        
        // Validar se CNPJ já existe em outra empresa
        if (!empresa.getCnpj().equals(empresaDTO.getCnpj()) && empresaParceiraRepository.existsByCnpj(empresaDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado: " + empresaDTO.getCnpj());
        }
        
        // Validar se email já existe em outra empresa
        if (!empresa.getEmail().equals(empresaDTO.getEmail()) && empresaParceiraRepository.existsByEmail(empresaDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + empresaDTO.getEmail());
        }
        
        // Atualizar campos
        empresa.setNome(empresaDTO.getNome());
        empresa.setEmail(empresaDTO.getEmail());
        if (empresaDTO.getSenha() != null && !empresaDTO.getSenha().isEmpty()) {
            empresa.setSenha(empresaDTO.getSenha()); // TODO: Implementar hash da senha
        }
        empresa.setCnpj(empresaDTO.getCnpj());
        empresa.setEmailContato(empresaDTO.getEmailContato());
        
        // Salvar
        EmpresaParceira empresaAtualizada = empresaParceiraRepository.save(empresa);
        
        // Retornar DTO
        return converterParaDTO(empresaAtualizada);
    }
    
    // Delete
    public void deletarEmpresa(Long id) {
        if (!empresaParceiraRepository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada com ID: " + id);
        }
        empresaParceiraRepository.deleteById(id);
    }
    
    // Método auxiliar para converter entidade para DTO
    private EmpresaParceiraDTO converterParaDTO(EmpresaParceira empresa) {
        EmpresaParceiraDTO dto = new EmpresaParceiraDTO();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setEmail(empresa.getEmail());
        dto.setCnpj(empresa.getCnpj());
        dto.setEmailContato(empresa.getEmailContato());
        return dto;
    }
}
