package com.merito.repository;

import com.merito.entity.Aluno;
import com.merito.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    Optional<Aluno> findByCpf(String cpf);
    
    Optional<Aluno> findByEmail(String email);
    
    List<Aluno> findByInstituicao(Instituicao instituicao);
    
    List<Aluno> findByCurso(String curso);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
}

