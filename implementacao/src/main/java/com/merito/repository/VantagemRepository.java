package com.merito.repository;

import com.merito.entity.EmpresaParceira;
import com.merito.entity.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    
    List<Vantagem> findByEmpresa(EmpresaParceira empresa);
    
    List<Vantagem> findByCustoEmMoedasLessThanEqual(Double custoMaximo);
    
    List<Vantagem> findByNomeContainingIgnoreCase(String nome);
}

