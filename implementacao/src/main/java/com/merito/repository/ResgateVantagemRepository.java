package com.merito.repository;

import com.merito.entity.Aluno;
import com.merito.entity.ResgateVantagem;
import com.merito.entity.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResgateVantagemRepository extends JpaRepository<ResgateVantagem, Long> {
    
    List<ResgateVantagem> findByAluno(Aluno aluno);
    
    List<ResgateVantagem> findByVantagem(Vantagem vantagem);
    
    Optional<ResgateVantagem> findByCodigoCupom(String codigoCupom);
    
    List<ResgateVantagem> findByDataResgateBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<ResgateVantagem> findByAlunoOrderByDataResgateDesc(Aluno aluno);
    
    boolean existsByCodigoCupom(String codigoCupom);
}

