# Sistema de Mérito Acadêmico

Sistema de gestão de mérito acadêmico baseado em moedas virtuais.

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6** ou superior
- **Node.js 18** ou superior
- **npm** ou **yarn**

### 1. Backend (Spring Boot)

```bash
# Navegue até o diretório do projeto
cd implementacao

# Compile e execute o backend
mvn spring-boot:run
```

O backend estará rodando em: **http://localhost:8080**

### 2. Frontend (Next.js)

Abra um novo terminal:

```bash
# Navegue até o diretório do frontend
cd implementacao/front

# Instale as dependências (primeira vez apenas)
npm install

# Execute o frontend
npm run dev
```

O frontend estará rodando em: **http://localhost:3000**

#### ⚠️ Problema no Windows PowerShell?

Se você receber o erro `PSSecurityException` ao executar `npm install`, é porque a política de execução de scripts está desabilitada. **Soluções:**

**Opção 1: Usar CMD ao invés de PowerShell**
- Abra o **Prompt de Comando (CMD)** ao invés do PowerShell
- Execute os comandos normalmente

**Opção 2: Habilitar execução de scripts no PowerShell (Recomendado)**
```powershell
# Execute como Administrador
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

**Opção 3: Usar npx diretamente**
```bash
npx npm install
```

## 🗄️ Acessar o Banco de Dados (H2 Console)

O projeto usa **H2 Database** em memória. Para acessar o console do banco:

1. Com o backend rodando, acesse: **http://localhost:8080/h2-console**

2. Preencha os campos de conexão:
   - **JDBC URL:** `jdbc:h2:mem:meritodb`
   - **User Name:** `sa`
   - **Password:** (deixe em branco)

3. Clique em **Connect**

4. Pronto! Você pode executar queries SQL diretamente no banco.

### Exemplo de Queries Úteis

```sql
-- Ver todas as tabelas
SHOW TABLES;

-- Listar todos os alunos
SELECT * FROM aluno;

-- Listar todas as empresas
SELECT * FROM empresa_parceira;

-- Listar todas as vantagens
SELECT * FROM vantagem;

-- Listar todos os usuários
SELECT * FROM usuario;
```

## 🔑 Credenciais de Acesso

### Administrador
- **Email:** `admin@admin.com`
- **Senha:** `admin123`

### Aluno (Exemplo)
- **Email:** `joao.silva@aluno.pucminas.br`
- **Senha:** `senha123`

> 📋 **Dados completos do aluno:** Veja o arquivo [ALUNO.md](./ALUNO.md)

### Professor (Exemplo)
- **Email:** `carlos.oliveira@pucminas.br`
- **Senha:** `prof123`
- **Nome:** Carlos Oliveira
- **CPF:** 123.456.789-00
- **Departamento:** Ciência da Computação
- **Saldo Inicial:** 1000.0 moedas (crédito inicial do sistema)
- **Instituição:** Instituição Padrão (mesma do aluno)

### Empresa (Exemplo)
- **Email:** `empresa@teste.com`
- **Senha:** `empresa123`

> 📋 **Dados completos da empresa parceira:** Veja o arquivo [EMPRESA_PARCEIRA.md](./EMPRESA_PARCEIRA.md)

## 📍 URLs Importantes

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **H2 Console:** http://localhost:8080/h2-console
- **Página do Professor:** http://localhost:3000/professor
- **Página de Vantagens (Empresa):** http://localhost:3000/empresa/vantagens
- **Página de Vantagens (Aluno):** http://localhost:3000/aluno/vantagens
- **Página de Resgates (Aluno):** http://localhost:3000/aluno/resgates

## ⚠️ Observações

- O banco de dados H2 é **em memória**, então os dados são perdidos quando o backend é reiniciado
- Os dados de exemplo são carregados automaticamente ao iniciar o backend
- Para produção, recomenda-se usar um banco de dados persistente (MySQL, PostgreSQL, etc.)
