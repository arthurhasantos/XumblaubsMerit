package com.merito.repository;

import com.merito.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {
    
    Optional<Instituicao> findByNome(String nome);
    
    boolean existsByNome(String nome);
}

