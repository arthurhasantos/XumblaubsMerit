package com.merito.repository;

import com.merito.entity.EmpresaParceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    
    Optional<EmpresaParceira> findByCnpj(String cnpj);
    
    Optional<EmpresaParceira> findByEmail(String email);
    
    Optional<EmpresaParceira> findByNome(String nome);
    
    boolean existsByCnpj(String cnpj);
    
    boolean existsByEmail(String email);
}

