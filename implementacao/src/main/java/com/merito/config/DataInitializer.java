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
            UsuarioRepository usuarioRepository) {
        
        return args -> {
            // Criar usuário ADMIN padrão
            Usuario admin = new Usuario();
            admin.setEmail("admin@admin.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTipoUsuario("ADMIN");
            usuarioRepository.save(admin);

            // Criar Instituição mínima (necessária para o aluno)
            Instituicao instituicaoPadrao = new Instituicao();
            instituicaoPadrao.setNome("Instituição Padrão");
            instituicaoPadrao.setEndereco("Endereço padrão");
            instituicaoRepository.save(instituicaoPadrao);

            // Criar Aluno
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

            // Criar Empresa Parceira
            EmpresaParceira empresa = new EmpresaParceira();
            empresa.setEmail("empresa@teste.com");
            empresa.setSenha(passwordEncoder.encode("empresa123"));
            empresa.setTipoUsuario("EMPRESA");
            empresa.setNome("Empresa Teste Parceira");
            empresa.setCnpj("11.222.333/0001-44");
            empresa.setEmailContato("contato@empresateste.com");
            empresaRepository.save(empresa);

            System.out.println("========================================");
            System.out.println("✓ Banco de dados inicializado!");
            System.out.println("✓ Usuários criados:");
            System.out.println("  - ADMIN: admin@admin.com / admin123");
            System.out.println("  - ALUNO: joao.silva@aluno.pucminas.br / senha123");
            System.out.println("  - EMPRESA: empresa@teste.com / empresa123");
            System.out.println("✓ Acesse o H2 Console em: http://localhost:8080/h2-console");
            System.out.println("✓ JDBC URL: jdbc:h2:mem:meritodb");
            System.out.println("✓ Username: sa");
            System.out.println("✓ Password: (deixe em branco)");
            System.out.println("========================================");
        };
    }
}

