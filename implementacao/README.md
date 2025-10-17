# Sistema de Mérito Acadêmico

Sistema de gestão de mérito acadêmico baseado em moedas virtuais, desenvolvido com Spring Boot, JPA e banco de dados H2.

## 📋 Descrição

Este sistema implementa um modelo de mérito acadêmico onde:
- **Professores** podem distribuir moedas virtuais aos alunos
- **Alunos** podem resgatar vantagens oferecidas por empresas parceiras usando suas moedas
- **Empresas Parceiras** cadastram vantagens que podem ser resgatadas pelos alunos
- **Instituições** gerenciam alunos e professores

## 🏗️ Estrutura do Projeto

O projeto segue o padrão de arquitetura em camadas:

```
implementacao/
├── src/main/java/com/merito/
│   ├── Application.java              # Classe principal da aplicação
│   ├── entity/                       # Entidades JPA
│   │   ├── Aluno.java
│   │   ├── EmpresaParceira.java
│   │   ├── Instituicao.java
│   │   ├── Professor.java
│   │   ├── ResgateVantagem.java
│   │   ├── Usuario.java
│   │   └── Vantagem.java
│   ├── repository/                   # Repositórios Spring Data JPA
│   │   ├── AlunoRepository.java
│   │   ├── EmpresaParceiraRepository.java
│   │   ├── InstituicaoRepository.java
│   │   ├── ProfessorRepository.java
│   │   ├── ResgateVantagemRepository.java
│   │   ├── UsuarioRepository.java
│   │   └── VantagemRepository.java
│   └── config/
│       └── DataInitializer.java      # Inicialização de dados de exemplo
├── src/main/resources/
│   └── application.properties        # Configurações da aplicação
└── pom.xml                           # Dependências Maven
```

## 🗄️ Modelo de Dados

### Entidades

1. **Instituicao**
   - Representa universidades/faculdades
   - Campos: id, nome, endereco

2. **Usuario** (classe base)
   - Classe abstrata para todos os tipos de usuários
   - Campos: id, email, senha, tipoUsuario
   - Herança: JOINED strategy

3. **Aluno** (extends Usuario)
   - Alunos matriculados em instituições
   - Campos: nome, cpf, rg, endereco, curso, saldoMoedas
   - Relacionamentos: pertence a uma Instituição, realiza Resgates

4. **Professor** (extends Usuario)
   - Professores das instituições
   - Campos: nome, cpf, departamento, saldoMoedas
   - Relacionamentos: pertence a uma Instituição

5. **EmpresaParceira** (extends Usuario)
   - Empresas que oferecem vantagens
   - Campos: nome, cnpj, emailContato
   - Relacionamentos: oferece Vantagens

6. **Vantagem**
   - Produtos/serviços oferecidos pelas empresas
   - Campos: nome, descricao, fotoUrl, custoEmMoedas
   - Relacionamentos: pertence a uma EmpresaParceira, pode ser resgatada

7. **ResgateVantagem**
   - Registro de resgates de vantagens por alunos
   - Campos: dataResgate, codigoCupom
   - Relacionamentos: realizado por Aluno, referencia uma Vantagem

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

### Passos para execução

1. **Navegue até o diretório do projeto:**
   ```bash
   cd implementacao
   ```

2. **Compile o projeto:**
   ```bash
   mvn clean install
   ```

3. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse o H2 Console:**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:meritodb`
   - Username: `sa`
   - Password: (deixe em branco)

## 🔧 Configuração

### application.properties

```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:meritodb
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

## 📊 Dados de Exemplo

A aplicação é inicializada com dados de exemplo através da classe `DataInitializer`:

- 2 Instituições (PUC Minas, UFMG)
- 2 Alunos
- 2 Professores
- 2 Empresas Parceiras
- 4 Vantagens
- 2 Resgates

## 🔍 Funcionalidades dos Repositórios

### InstituicaoRepository
- `findByNome(String nome)` - Buscar instituição por nome
- `existsByNome(String nome)` - Verificar se instituição existe

### UsuarioRepository
- `findByEmail(String email)` - Buscar usuário por email
- `findByEmailAndSenha(String email, String senha)` - Autenticação
- `existsByEmail(String email)` - Verificar se email existe

### AlunoRepository
- `findByCpf(String cpf)` - Buscar aluno por CPF
- `findByInstituicao(Instituicao instituicao)` - Listar alunos de uma instituição
- `findByCurso(String curso)` - Listar alunos por curso

### ProfessorRepository
- `findByCpf(String cpf)` - Buscar professor por CPF
- `findByInstituicao(Instituicao instituicao)` - Listar professores de uma instituição
- `findByDepartamento(String departamento)` - Listar professores por departamento

### EmpresaParceiraRepository
- `findByCnpj(String cnpj)` - Buscar empresa por CNPJ
- `findByNome(String nome)` - Buscar empresa por nome

### VantagemRepository
- `findByEmpresa(EmpresaParceira empresa)` - Listar vantagens de uma empresa
- `findByCustoEmMoedasLessThanEqual(Double custoMaximo)` - Buscar vantagens por faixa de preço
- `findByNomeContainingIgnoreCase(String nome)` - Buscar vantagens por nome

### ResgateVantagemRepository
- `findByAluno(Aluno aluno)` - Listar resgates de um aluno
- `findByCodigoCupom(String codigoCupom)` - Buscar resgate por cupom
- `findByDataResgateBetween(LocalDateTime inicio, LocalDateTime fim)` - Buscar resgates por período
- `findByAlunoOrderByDataResgateDesc(Aluno aluno)` - Histórico de resgates do aluno

## 🛠️ Tecnologias Utilizadas

- **Spring Boot 3.1.5** - Framework principal
- **Spring Data JPA** - Abstração de acesso a dados
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências

**Nota:** Este projeto **não usa Lombok**. Todos os getters, setters e construtores foram implementados manualmente para garantir máxima compatibilidade e facilidade de debugging.

## 📝 Estratégia de Herança

O projeto utiliza a estratégia **JOINED** para herança JPA:
- Tabela `usuario` contém os campos comuns
- Tabelas `aluno`, `professor` e `empresa_parceira` contêm campos específicos
- Relacionamento via chave estrangeira que também é chave primária

## 🔐 Observações de Segurança

⚠️ **ATENÇÃO**: Este é um projeto de demonstração. Para uso em produção:
- Implemente hash de senhas (BCrypt)
- Adicione autenticação JWT/OAuth2
- Configure HTTPS
- Implemente validações de entrada
- Adicione tratamento de exceções robusto
- Configure CORS adequadamente

## 📄 Licença

Este projeto é parte de um sistema acadêmico de demonstração.

