package com.merito.repository;

import com.merito.entity.Instituicao;
import com.merito.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    Optional<Professor> findByCpf(String cpf);
    
    Optional<Professor> findByEmail(String email);
    
    List<Professor> findByInstituicao(Instituicao instituicao);
    
    List<Professor> findByDepartamento(String departamento);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
}

