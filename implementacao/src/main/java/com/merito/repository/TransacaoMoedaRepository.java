package com.merito.repository;

import com.merito.entity.Aluno;
import com.merito.entity.Professor;
import com.merito.entity.TransacaoMoeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoMoedaRepository extends JpaRepository<TransacaoMoeda, Long> {
    
    List<TransacaoMoeda> findByProfessorOrderByDataTransacaoDesc(Professor professor);
    
    List<TransacaoMoeda> findByAlunoOrderByDataTransacaoDesc(Aluno aluno);
    
    List<TransacaoMoeda> findByProfessorAndTipoOrderByDataTransacaoDesc(Professor professor, String tipo);
    
    List<TransacaoMoeda> findByAlunoAndTipoOrderByDataTransacaoDesc(Aluno aluno, String tipo);
}

