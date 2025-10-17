package com.merito.config;

import com.merito.entity.*;
import com.merito.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            InstituicaoRepository instituicaoRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            EmpresaParceiraRepository empresaRepository,
            VantagemRepository vantagemRepository,
            ResgateVantagemRepository resgateRepository) {
        
        return args -> {
            // Criar Instituições
            Instituicao pucMinas = new Instituicao();
            pucMinas.setNome("PUC Minas");
            pucMinas.setEndereco("Av. Dom José Gaspar, 500 - Coração Eucarístico, Belo Horizonte - MG");
            instituicaoRepository.save(pucMinas);

            Instituicao ufmg = new Instituicao();
            ufmg.setNome("UFMG");
            ufmg.setEndereco("Av. Presidente Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG");
            instituicaoRepository.save(ufmg);

            // Criar Alunos
            Aluno aluno1 = new Aluno();
            aluno1.setEmail("joao.silva@aluno.pucminas.br");
            aluno1.setSenha("senha123");
            aluno1.setTipoUsuario("ALUNO");
            aluno1.setNome("João Silva");
            aluno1.setCpf("111.222.333-44");
            aluno1.setRg("MG-12.345.678");
            aluno1.setEndereco("Rua A, 123 - Belo Horizonte");
            aluno1.setCurso("Engenharia de Software");
            aluno1.setSaldoMoedas(1000.0);
            aluno1.setInstituicao(pucMinas);
            alunoRepository.save(aluno1);

            Aluno aluno2 = new Aluno();
            aluno2.setEmail("maria.santos@aluno.pucminas.br");
            aluno2.setSenha("senha456");
            aluno2.setTipoUsuario("ALUNO");
            aluno2.setNome("Maria Santos");
            aluno2.setCpf("555.666.777-88");
            aluno2.setRg("MG-98.765.432");
            aluno2.setEndereco("Rua B, 456 - Belo Horizonte");
            aluno2.setCurso("Ciência da Computação");
            aluno2.setSaldoMoedas(500.0);
            aluno2.setInstituicao(pucMinas);
            alunoRepository.save(aluno2);

            // Criar Professores
            Professor prof1 = new Professor();
            prof1.setEmail("carlos.oliveira@pucminas.br");
            prof1.setSenha("prof123");
            prof1.setTipoUsuario("PROFESSOR");
            prof1.setNome("Carlos Oliveira");
            prof1.setCpf("999.888.777-66");
            prof1.setDepartamento("Departamento de Engenharia de Software");
            prof1.setSaldoMoedas(5000.0);
            prof1.setInstituicao(pucMinas);
            professorRepository.save(prof1);

            Professor prof2 = new Professor();
            prof2.setEmail("ana.costa@ufmg.br");
            prof2.setSenha("prof456");
            prof2.setTipoUsuario("PROFESSOR");
            prof2.setNome("Ana Costa");
            prof2.setCpf("444.333.222-11");
            prof2.setDepartamento("Departamento de Ciência da Computação");
            prof2.setSaldoMoedas(3000.0);
            prof2.setInstituicao(ufmg);
            professorRepository.save(prof2);

            // Criar Empresas Parceiras
            EmpresaParceira empresa1 = new EmpresaParceira();
            empresa1.setEmail("contato@techstore.com.br");
            empresa1.setSenha("emp123");
            empresa1.setTipoUsuario("EMPRESA");
            empresa1.setNome("Tech Store");
            empresa1.setCnpj("12.345.678/0001-90");
            empresa1.setEmailContato("parceria@techstore.com.br");
            empresaRepository.save(empresa1);

            EmpresaParceira empresa2 = new EmpresaParceira();
            empresa2.setEmail("contato@livrariaonline.com.br");
            empresa2.setSenha("emp456");
            empresa2.setTipoUsuario("EMPRESA");
            empresa2.setNome("Livraria Online");
            empresa2.setCnpj("98.765.432/0001-10");
            empresa2.setEmailContato("comercial@livrariaonline.com.br");
            empresaRepository.save(empresa2);

            // Criar Vantagens
            Vantagem vantagem1 = new Vantagem();
            vantagem1.setNome("Desconto de 20% em Mouse Gamer");
            vantagem1.setDescricao("Ganhe 20% de desconto na compra de qualquer mouse gamer da nossa loja");
            vantagem1.setFotoUrl("https://exemplo.com/mouse-gamer.jpg");
            vantagem1.setCustoEmMoedas(200.0);
            vantagem1.setEmpresa(empresa1);
            vantagemRepository.save(vantagem1);

            Vantagem vantagem2 = new Vantagem();
            vantagem2.setNome("Teclado Mecânico com 50% OFF");
            vantagem2.setDescricao("Desconto de 50% na compra de teclados mecânicos selecionados");
            vantagem2.setFotoUrl("https://exemplo.com/teclado.jpg");
            vantagem2.setCustoEmMoedas(500.0);
            vantagem2.setEmpresa(empresa1);
            vantagemRepository.save(vantagem2);

            Vantagem vantagem3 = new Vantagem();
            vantagem3.setNome("Livro de Programação Grátis");
            vantagem3.setDescricao("Ganhe um livro de programação de sua escolha");
            vantagem3.setFotoUrl("https://exemplo.com/livro.jpg");
            vantagem3.setCustoEmMoedas(300.0);
            vantagem3.setEmpresa(empresa2);
            vantagemRepository.save(vantagem3);

            Vantagem vantagem4 = new Vantagem();
            vantagem4.setNome("Assinatura Premium 3 Meses");
            vantagem4.setDescricao("3 meses de acesso premium à plataforma de e-books técnicos");
            vantagem4.setFotoUrl("https://exemplo.com/premium.jpg");
            vantagem4.setCustoEmMoedas(800.0);
            vantagem4.setEmpresa(empresa2);
            vantagemRepository.save(vantagem4);

            // Criar Resgates
            ResgateVantagem resgate1 = new ResgateVantagem();
            resgate1.setDataResgate(LocalDateTime.now().minusDays(5));
            resgate1.setCodigoCupom(UUID.randomUUID().toString());
            resgate1.setAluno(aluno1);
            resgate1.setVantagem(vantagem1);
            resgateRepository.save(resgate1);

            ResgateVantagem resgate2 = new ResgateVantagem();
            resgate2.setDataResgate(LocalDateTime.now().minusDays(2));
            resgate2.setCodigoCupom(UUID.randomUUID().toString());
            resgate2.setAluno(aluno2);
            resgate2.setVantagem(vantagem3);
            resgateRepository.save(resgate2);

            System.out.println("========================================");
            System.out.println("✓ Banco de dados inicializado com dados de exemplo!");
            System.out.println("✓ Acesse o H2 Console em: http://localhost:8080/h2-console");
            System.out.println("✓ JDBC URL: jdbc:h2:mem:meritodb");
            System.out.println("✓ Username: sa");
            System.out.println("✓ Password: (deixe em branco)");
            System.out.println("========================================");
        };
    }
}

