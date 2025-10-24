package com.merito.service;

import com.merito.dto.EmpresaParceiraDTO;
import com.merito.dto.EmpresaUpdateDTO;
import com.merito.entity.EmpresaParceira;
import com.merito.repository.EmpresaParceiraRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
        empresa.setSenha(passwordEncoder.encode(empresaDTO.getSenha())); // Hash da senha
        empresa.setTipoUsuario("EMPRESA");
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
    public EmpresaParceiraDTO atualizarEmpresa(Long id, EmpresaUpdateDTO empresaUpdateDTO) {
        EmpresaParceira empresa = empresaParceiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + id));
        
        // Validar se CNPJ já existe em outra empresa
        if (!empresa.getCnpj().equals(empresaUpdateDTO.getCnpj()) && empresaParceiraRepository.existsByCnpj(empresaUpdateDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado: " + empresaUpdateDTO.getCnpj());
        }
        
        // Validar se email já existe em outra empresa
        if (!empresa.getEmail().equals(empresaUpdateDTO.getEmail()) && empresaParceiraRepository.existsByEmail(empresaUpdateDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + empresaUpdateDTO.getEmail());
        }
        
        // Atualizar campos
        empresa.setNome(empresaUpdateDTO.getNome());
        empresa.setEmail(empresaUpdateDTO.getEmail());
        if (empresaUpdateDTO.getSenha() != null && !empresaUpdateDTO.getSenha().isEmpty()) {
            empresa.setSenha(passwordEncoder.encode(empresaUpdateDTO.getSenha())); // Hash da senha
        }
        empresa.setCnpj(empresaUpdateDTO.getCnpj());
        empresa.setEmailContato(empresaUpdateDTO.getEmailContato());
        
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
