# Sistema de Mérito Acadêmico

Sistema de gestão de mérito acadêmico baseado em moedas virtuais, desenvolvido com **Spring Boot** (backend) e **Next.js** (frontend), utilizando JPA e banco de dados H2.

## 📋 Descrição

Este sistema implementa um modelo de mérito acadêmico onde:
- **Professores** podem distribuir moedas virtuais aos alunos
- **Alunos** podem resgatar vantagens oferecidas por empresas parceiras usando suas moedas
- **Empresas Parceiras** cadastram vantagens que podem ser resgatadas pelos alunos
- **Instituições** gerenciam alunos e professores

## 🏗️ Estrutura do Projeto

O projeto segue o padrão de arquitetura em camadas com **Backend** (Spring Boot) e **Frontend** (Next.js):

```
implementacao/
├── src/main/java/com/merito/         # BACKEND - Spring Boot
│   ├── Application.java              # Classe principal da aplicação
│   ├── config/                       # Configurações
│   │   ├── DataInitializer.java      # Inicialização de dados de exemplo
│   │   └── WebConfig.java            # Configuração CORS
│   ├── controller/                   # Controllers REST
│   │   ├── AlunoController.java      # CRUD Aluno
│   │   ├── EmpresaParceiraController.java # CRUD Empresa Parceira
│   │   ├── InstituicaoController.java # CRUD Instituição
│   │   └── TestController.java       # Endpoints de teste
│   ├── dto/                          # Data Transfer Objects
│   │   ├── AlunoDTO.java             # DTO para Aluno
│   │   └── EmpresaParceiraDTO.java   # DTO para Empresa Parceira
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
│   └── service/                      # Camada de Serviço
│       ├── AlunoService.java         # Lógica de negócio Aluno
│       └── EmpresaParceiraService.java # Lógica de negócio Empresa
├── front/                            # FRONTEND - Next.js
│   ├── app/                          # App Router (Next.js 13+)
│   │   ├── layout.tsx                # Layout principal
│   │   ├── page.tsx                  # Página inicial
│   │   └── signin/                   # Páginas de autenticação
│   ├── components/                   # Componentes React
│   │   ├── Header/                   # Cabeçalho
│   │   ├── Footer/                   # Rodapé
│   │   └── Auth/                     # Componentes de autenticação
│   ├── contexts/                     # Contextos React
│   ├── lib/                          # Utilitários
│   ├── public/                       # Arquivos estáticos
│   ├── styles/                       # Estilos CSS
│   └── types/                        # Tipos TypeScript
├── src/main/resources/
│   └── application.properties        # Configurações da aplicação
├── pom.xml                           # Dependências Maven (Backend)
├── package.json                      # Dependências NPM (Frontend)
└── API_CRUD_DOCUMENTATION.md         # Documentação da API
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

**Backend:**
- Java 17 ou superior
- Maven 3.6 ou superior

**Frontend:**
- Node.js 18 ou superior
- npm ou yarn

### Passos para execução

#### **1. Backend (Spring Boot)**

```bash
# Navegue até o diretório do projeto
cd implementacao

# Compile o projeto
mvn clean compile

# Execute a aplicação
mvn spring-boot:run
```

#### **2. Frontend (Next.js)**

```bash
# Navegue até o diretório do frontend
cd implementacao/front

# Instale as dependências (primeira vez)
npm install

# Execute em modo desenvolvimento
npm run dev
```

### **🌐 URLs de Acesso**

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:meritodb`
  - Username: `sa`
  - Password: (deixe em branco)

### **🔑 Credenciais de Acesso**

**Usuário Administrador:**
- **Email:** `admin@admin.com`
- **Senha:** `admin123`

**Usuários de Exemplo:**
- **Aluno:** `joao.silva@aluno.pucminas.br` / `senha123`
- **Professor:** `carlos.oliveira@pucminas.br` / `prof123`
- **Empresa:** `contato@techstore.com.br` / `emp123`

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

## 🚀 Funcionalidades Implementadas

### **CRUDs Completos**
- ✅ **CRUD Aluno** - Cadastro, listagem, busca, atualização e exclusão
- ✅ **CRUD Empresa Parceira** - Cadastro, listagem, busca, atualização e exclusão
- ✅ **CRUD Instituição** - Operações básicas de instituições

### **API REST**
- ✅ **17 endpoints** implementados
- ✅ **Validação de dados** com Bean Validation
- ✅ **Tratamento de erros** padronizado
- ✅ **CORS configurado** para frontend
- ✅ **Documentação da API** completa
- ✅ **Autenticação JWT** implementada
- ✅ **Controle de acesso** por roles (ADMIN)
- ✅ **Hash de senhas** com BCrypt

### **Frontend**
- ✅ **Interface responsiva** com Tailwind CSS
- ✅ **Tema escuro/claro** com Next Themes
- ✅ **Componentes reutilizáveis** organizados
- ✅ **Formulários** com React Hook Form
- ✅ **Notificações** com React Hot Toast
- ✅ **Autenticação** com Context API
- ✅ **Modais** para CRUD operations
- ✅ **Proteção de rotas** por roles

### **Banco de Dados**
- ✅ **H2 em memória** para desenvolvimento
- ✅ **Console H2** para consultas SQL
- ✅ **Dados de exemplo** carregados automaticamente
- ✅ **Logs SQL** para debugging

## 🛠️ Tecnologias Utilizadas

### **Backend (Spring Boot)**
- **Spring Boot 3.1.5** - Framework principal
- **Spring Data JPA** - Abstração de acesso a dados
- **Spring Boot Validation** - Validação de dados
- **Spring Security** - Autenticação e autorização
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **BCrypt** - Hash de senhas
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências
- **Java 17** - Linguagem de programação

### **Frontend (Next.js)**
- **Next.js 13.5.6** - Framework React
- **React 18.2.0** - Biblioteca de interface
- **TypeScript 5.2.2** - Tipagem estática
- **Tailwind CSS 3.3.5** - Framework CSS
- **React Hook Form 7.47.0** - Gerenciamento de formulários
- **React Hot Toast 2.4.1** - Notificações
- **Next Themes 0.2.1** - Gerenciamento de temas

### **Ferramentas de Desenvolvimento**
- **ESLint** - Linting de código
- **Prettier** - Formatação de código
- **PostCSS** - Processamento CSS
- **Autoprefixer** - Prefixos CSS automáticos

**Nota:** Este projeto **não usa Lombok**. Todos os getters, setters e construtores foram implementados manualmente para garantir máxima compatibilidade e facilidade de debugging.

## 📝 Estratégia de Herança

O projeto utiliza a estratégia **JOINED** para herança JPA:
- Tabela `usuario` contém os campos comuns
- Tabelas `aluno`, `professor` e `empresa_parceira` contêm campos específicos
- Relacionamento via chave estrangeira que também é chave primária

## 🎯 Próximos Passos

### **Sprint Atual - Concluída ✅**
- ✅ CRUDs de Aluno e Empresa Parceira
- ✅ Frontend básico com Next.js
- ✅ Configuração H2 para desenvolvimento
- ✅ Documentação da API
- ✅ Autenticação JWT implementada
- ✅ Controle de acesso por roles (ADMIN)
- ✅ Hash de senhas com BCrypt
- ✅ Modais elegantes para CRUD
- ✅ Notificações com React Hot Toast

### **Próximas Sprints**
- 🔄 **Autenticação e Segurança** ✅ **CONCLUÍDO**
  - ✅ Implementar JWT
  - ✅ Hash de senhas com BCrypt
  - ✅ Controle de acesso por roles
- 🔄 **CRUDs Restantes**
  - CRUD Professor
  - CRUD Vantagem
  - CRUD ResgateVantagem
- 🔄 **Funcionalidades de Negócio**
  - Distribuição de moedas
  - Sistema de notificações por email
  - Relatórios e dashboards
- 🔄 **Melhorias no Frontend**
  - Páginas de CRUD completas
  - Dashboard administrativo
  - Interface para alunos e professores

## 🔐 Observações de Segurança

⚠️ **ATENÇÃO**: Este é um projeto de demonstração. Para uso em produção:
- ✅ **Hash de senhas (BCrypt)** - Implementado
- ✅ **Autenticação JWT** - Implementado
- 🔄 Configure HTTPS
- ✅ **Validações de entrada** - Implementado com Bean Validation
- ✅ **Tratamento de exceções** - Implementado
- ✅ **CORS configurado** - Implementado

## 📄 Licença

Este projeto é parte de um sistema acadêmico de demonstração desenvolvido para o curso de LDS (Laboratório de Desenvolvimento de Software) da PUC Minas.

