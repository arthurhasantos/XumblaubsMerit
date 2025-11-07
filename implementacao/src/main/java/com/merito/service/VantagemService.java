package com.merito.service;

import com.merito.dto.VantagemDTO;
import com.merito.entity.EmpresaParceira;
import com.merito.entity.Vantagem;
import com.merito.repository.EmpresaParceiraRepository;
import com.merito.repository.VantagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VantagemService {
    
    @Autowired
    private VantagemRepository vantagemRepository;
    
    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;
    
    // Create - Criar vantagem para uma empresa
    public VantagemDTO criarVantagem(VantagemDTO vantagemDTO, Long empresaId) {
        // Buscar empresa
        EmpresaParceira empresa = empresaParceiraRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + empresaId));
        
        // Criar entidade
        Vantagem vantagem = new Vantagem();
        vantagem.setNome(vantagemDTO.getNome());
        vantagem.setDescricao(vantagemDTO.getDescricao());
        vantagem.setFotoUrl(vantagemDTO.getFotoUrl());
        vantagem.setCustoEmMoedas(vantagemDTO.getCustoEmMoedas());
        vantagem.setEmpresa(empresa);
        
        // Salvar
        Vantagem vantagemSalva = vantagemRepository.save(vantagem);
        
        // Retornar DTO
        return converterParaDTO(vantagemSalva);
    }
    
    // Read - Listar todas as vantagens
    public List<VantagemDTO> listarTodasVantagens() {
        return vantagemRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Read - Listar vantagens de uma empresa específica
    public List<VantagemDTO> listarVantagensPorEmpresa(Long empresaId) {
        EmpresaParceira empresa = empresaParceiraRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + empresaId));
        
        return vantagemRepository.findByEmpresa(empresa).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Read - Buscar vantagem por ID
    public Optional<VantagemDTO> buscarVantagemPorId(Long id) {
        return vantagemRepository.findById(id)
                .map(this::converterParaDTO);
    }
    
    // Update - Atualizar vantagem
    public VantagemDTO atualizarVantagem(Long id, VantagemDTO vantagemDTO, Long empresaId) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + id));
        
        // Verificar se a vantagem pertence à empresa
        if (!vantagem.getEmpresa().getId().equals(empresaId)) {
            throw new RuntimeException("Vantagem não pertence à empresa informada");
        }
        
        // Atualizar campos
        vantagem.setNome(vantagemDTO.getNome());
        vantagem.setDescricao(vantagemDTO.getDescricao());
        vantagem.setFotoUrl(vantagemDTO.getFotoUrl());
        vantagem.setCustoEmMoedas(vantagemDTO.getCustoEmMoedas());
        
        // Salvar
        Vantagem vantagemAtualizada = vantagemRepository.save(vantagem);
        
        // Retornar DTO
        return converterParaDTO(vantagemAtualizada);
    }
    
    // Delete - Deletar vantagem
    public void deletarVantagem(Long id, Long empresaId) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + id));
        
        // Verificar se a vantagem pertence à empresa
        if (!vantagem.getEmpresa().getId().equals(empresaId)) {
            throw new RuntimeException("Vantagem não pertence à empresa informada");
        }
        
        vantagemRepository.deleteById(id);
    }
    
    // Método auxiliar para converter entidade para DTO
    private VantagemDTO converterParaDTO(Vantagem vantagem) {
        VantagemDTO dto = new VantagemDTO();
        dto.setId(vantagem.getId());
        dto.setNome(vantagem.getNome());
        dto.setDescricao(vantagem.getDescricao());
        dto.setFotoUrl(vantagem.getFotoUrl());
        dto.setCustoEmMoedas(vantagem.getCustoEmMoedas());
        dto.setEmpresaId(vantagem.getEmpresa().getId());
        return dto;
    }
}



