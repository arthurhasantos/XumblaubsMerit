package com.merito.config;

import com.merito.entity.*;
import com.merito.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(
            InstituicaoRepository instituicaoRepository,
            AlunoRepository alunoRepository,
            EmpresaParceiraRepository empresaRepository,
            ProfessorRepository professorRepository,
            UsuarioRepository usuarioRepository,
            com.merito.repository.TransacaoMoedaRepository transacaoMoedaRepository) {
        
        return args -> {
            // Criar usuário ADMIN padrão (se não existir)
            if (!usuarioRepository.findByEmail("admin@admin.com").isPresent()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@admin.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setTipoUsuario("ADMIN");
                usuarioRepository.save(admin);
            }

            // Criar Instituição mínima (necessária para o aluno e professor)
            Instituicao instituicaoPadrao = instituicaoRepository.findAll().stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Instituicao inst = new Instituicao();
                        inst.setNome("Instituição Padrão");
                        inst.setEndereco("Endereço padrão");
                        return instituicaoRepository.save(inst);
                    });

            // Criar Aluno (se não existir)
            if (!alunoRepository.findByEmail("joao.silva@aluno.pucminas.br").isPresent()) {
                Aluno aluno = new Aluno();
                aluno.setEmail("joao.silva@aluno.pucminas.br");
                aluno.setSenha(passwordEncoder.encode("senha123"));
                aluno.setTipoUsuario("ALUNO");
                aluno.setNome("João Silva");
                aluno.setCpf("111.222.333-44");
                aluno.setRg("MG-12.345.678");
                aluno.setEndereco("Rua A, 123 - Belo Horizonte");
                aluno.setCurso("Engenharia de Software");
                aluno.setSaldoMoedas(1000.0);
                aluno.setInstituicao(instituicaoPadrao);
                alunoRepository.save(aluno);
            }

            // Criar Aluno 2 - Victor (se não existir)
            if (!alunoRepository.findByEmail("victorferreiralmeida@gmail.com").isPresent()) {
                Aluno aluno2 = new Aluno();
                aluno2.setEmail("victorferreiralmeida@gmail.com");
                aluno2.setSenha(passwordEncoder.encode("senha123"));
                aluno2.setTipoUsuario("ALUNO");
                aluno2.setNome("Victor Ferreira de Almeida");
                aluno2.setCpf("222.333.444-55");
                aluno2.setRg("MG-23.456.789");
                aluno2.setEndereco("Rua B, 456 - Belo Horizonte");
                aluno2.setCurso("Engenharia de Software");
                aluno2.setSaldoMoedas(1000.0);
                aluno2.setInstituicao(instituicaoPadrao);
                alunoRepository.save(aluno2);
            }

            // Criar Empresa Parceira (se não existir)
            if (!empresaRepository.findByEmail("empresa@teste.com").isPresent()) {
                EmpresaParceira empresa = new EmpresaParceira();
                empresa.setEmail("empresa@teste.com");
                empresa.setSenha(passwordEncoder.encode("empresa123"));
                empresa.setTipoUsuario("EMPRESA");
                empresa.setNome("Empresa Teste Parceira");
                empresa.setCnpj("11.222.333/0001-44");
                empresa.setEmailContato("contato@empresateste.com");
                empresaRepository.save(empresa);
            }

            // Criar Empresa Parceira 2 - Lucas (se não existir)
            if (!empresaRepository.findByEmail("lucas.jacome66@gmail.com").isPresent()) {
                EmpresaParceira empresa2 = new EmpresaParceira();
                empresa2.setEmail("lucas.jacome66@gmail.com");
                empresa2.setSenha(passwordEncoder.encode("empresa123"));
                empresa2.setTipoUsuario("EMPRESA");
                empresa2.setNome("Empresa Lucas Jácome");
                empresa2.setCnpj("22.333.444/0001-55");
                empresa2.setEmailContato("lucas.jacome66@gmail.com");
                empresaRepository.save(empresa2);
            }

            // Criar Professor (se não existir)
            if (!professorRepository.findByEmail("carlos.oliveira@pucminas.br").isPresent()) {
                Professor professor = new Professor();
                professor.setEmail("carlos.oliveira@pucminas.br");
                professor.setSenha(passwordEncoder.encode("prof123"));
                professor.setTipoUsuario("PROFESSOR");
                professor.setNome("Carlos Oliveira");
                professor.setCpf("123.456.789-00");
                professor.setDepartamento("Ciência da Computação");
                professor.setSaldoMoedas(1000.0); // Saldo inicial de 1000 moedas
                professor.setInstituicao(instituicaoPadrao);
                Professor professorSalvo = professorRepository.save(professor);
                
                // Registrar crédito inicial no histórico
                com.merito.entity.TransacaoMoeda transacaoInicial = new com.merito.entity.TransacaoMoeda();
                transacaoInicial.setDataTransacao(java.time.LocalDateTime.now());
                transacaoInicial.setQuantidade(1000.0);
                transacaoInicial.setTipo("RECEBIDA");
                transacaoInicial.setMotivo("Crédito inicial do sistema");
                transacaoInicial.setProfessor(professorSalvo);
                transacaoInicial.setAluno(null); // Não há aluno envolvido
                transacaoMoedaRepository.save(transacaoInicial);
            }

            // Criar Professor 3 (se não existir)
            if (!professorRepository.findByEmail("deoxualt@gmail.com").isPresent()) {
                Professor professor3 = new Professor();
                professor3.setEmail("deoxualt@gmail.com");
                professor3.setSenha(passwordEncoder.encode("prof123"));
                professor3.setTipoUsuario("PROFESSOR");
                professor3.setNome("teste");
                professor3.setCpf("122.456.789-00");
                professor3.setDepartamento("Foudass da Computação");
                professor3.setSaldoMoedas(1000.0); // Saldo inicial de 1000 moedas
                professor3.setInstituicao(instituicaoPadrao);
                Professor professorSalvo = professorRepository.save(professor3);
                
                // Registrar crédito inicial no histórico
                com.merito.entity.TransacaoMoeda transacaoInicial = new com.merito.entity.TransacaoMoeda();
                transacaoInicial.setDataTransacao(java.time.LocalDateTime.now());
                transacaoInicial.setQuantidade(1000.0);
                transacaoInicial.setTipo("RECEBIDA");
                transacaoInicial.setMotivo("Crédito inicial do sistema");
                transacaoInicial.setProfessor(professorSalvo);
                transacaoInicial.setAluno(null); // Não há aluno envolvido
                transacaoMoedaRepository.save(transacaoInicial);
            }
            
            // Criar Professor 2 - Arthur (se não existir)
            if (!professorRepository.findByEmail("arthurhsantos2018@gmail.com").isPresent()) {
                Professor professor2 = new Professor();
                professor2.setEmail("arthurhsantos2018@gmail.com");
                professor2.setSenha(passwordEncoder.encode("prof123"));
                professor2.setTipoUsuario("PROFESSOR");
                professor2.setNome("Arthur Henrique Araújo Santos");
                professor2.setCpf("234.567.890-11");
                professor2.setDepartamento("Ciência da Computação");
                professor2.setSaldoMoedas(1000.0); // Saldo inicial de 1000 moedas
                professor2.setInstituicao(instituicaoPadrao);
                Professor professor2Salvo = professorRepository.save(professor2);
                
                // Registrar crédito inicial no histórico
                com.merito.entity.TransacaoMoeda transacaoInicial2 = new com.merito.entity.TransacaoMoeda();
                transacaoInicial2.setDataTransacao(java.time.LocalDateTime.now());
                transacaoInicial2.setQuantidade(1000.0);
                transacaoInicial2.setTipo("RECEBIDA");
                transacaoInicial2.setMotivo("Crédito inicial do sistema");
                transacaoInicial2.setProfessor(professor2Salvo);
                transacaoInicial2.setAluno(null); // Não há aluno envolvido
                transacaoMoedaRepository.save(transacaoInicial2);
            }

            System.out.println("========================================");
            System.out.println("✓ Banco de dados inicializado!");
            System.out.println("✓ Usuários criados:");
            System.out.println("  - ADMIN: admin@admin.com / admin123");
            System.out.println("  - ALUNO: joao.silva@aluno.pucminas.br / senha123");
            System.out.println("  - ALUNO 2: victorferreiralmeida@gmail.com / senha123");
            System.out.println("  - PROFESSOR: carlos.oliveira@pucminas.br / prof123");
            System.out.println("  - PROFESSOR 2: arthurhsantos2018@gmail.com / prof123");
            System.out.println("  - EMPRESA: empresa@teste.com / empresa123");
            System.out.println("  - EMPRESA 2: lucas.jacome66@gmail.com / empresa123");
            System.out.println("✓ Acesse o H2 Console em: http://localhost:8080/h2-console");
            System.out.println("✓ JDBC URL: jdbc:h2:mem:meritodb");
            System.out.println("✓ Username: sa");
            System.out.println("✓ Password: (deixe em branco)");
            System.out.println("========================================");
        };
    }
}

