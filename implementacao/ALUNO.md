# 📋 Dados de Acesso - Aluno

## 👨‍🎓 Aluno Pré-Cadastrado

### João Silva

**Dados de Login:**
- **Email:** `joao.silva@aluno.pucminas.br`
- **Senha:** `senha123`
- **Tipo de Usuário:** ALUNO

**Dados Cadastrais:**
- **Nome:** João Silva
- **CPF:** 111.222.333-44
- **RG:** MG-12.345.678
- **Endereço:** Rua A, 123 - Belo Horizonte
- **Curso:** Engenharia de Software
- **Instituição:** PUC Minas
- **Saldo de Moedas:** 1000.0 moedas

---

## 🔐 Como Acessar

### 1. Via Frontend

1. Acesse: **http://localhost:3000/signin**
2. Faça login com:
   - Email: `joao.silva@aluno.pucminas.br`
   - Senha: `senha123`
3. Após o login, você será redirecionado automaticamente para: **http://localhost:3000/aluno/vantagens**

**Páginas disponíveis:**
- **Vantagens Disponíveis:** http://localhost:3000/aluno/vantagens
- **Meus Resgates:** http://localhost:3000/aluno/resgates

### 2. Via API

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.silva@aluno.pucminas.br",
    "senha": "senha123"
  }'
```

---

## 📍 Funcionalidades Disponíveis

Após fazer login como aluno, você pode:

- ✅ **Visualizar Vantagens** - Ver todas as vantagens disponíveis no sistema
- ✅ **Ver Saldo** - Consultar seu saldo atual de moedas
- ✅ **Expandir Imagens** - Clicar nas imagens para vê-las em tamanho maior
- ✅ **Resgatar Vantagens** - Resgatar vantagens usando suas moedas
- ✅ **Ver Meus Resgates** - Visualizar histórico de vantagens resgatadas com códigos de cupom

---

## 🎯 Endpoints da API

### Listar Todas as Vantagens
```bash
GET http://localhost:8080/api/vantagens
Authorization: Bearer {token}
```

### Resgatar Vantagem
```bash
POST http://localhost:8080/api/resgates
Authorization: Bearer {token}
Content-Type: application/json

{
  "vantagemId": 1
}
```

### Listar Meus Resgates
```bash
GET http://localhost:8080/api/resgates/meus
Authorization: Bearer {token}
```

### Buscar Aluno por Email
```bash
GET http://localhost:8080/api/alunos/email/joao.silva@aluno.pucminas.br
Authorization: Bearer {token}
```

### Buscar Aluno por ID
```bash
GET http://localhost:8080/api/alunos/{id}
Authorization: Bearer {token}
```

---

## 📊 Informações do Aluno

### Saldo de Moedas
O aluno **João Silva** possui **1000.0 moedas** disponíveis para resgatar vantagens.

### Vantagens Disponíveis
O sistema lista todas as vantagens cadastradas pelas empresas parceiras, mostrando:
- Nome da vantagem
- Descrição
- Foto (se disponível)
- Custo em moedas
- Status de disponibilidade (se o aluno tem saldo suficiente)

---

## 🔄 Outros Alunos Cadastrados

O sistema também possui outro aluno de exemplo:

**Maria Santos**
- Email: `maria.santos@aluno.pucminas.br`
- Senha: `senha456`
- Curso: Ciência da Computação
- Saldo: 500.0 moedas

---

## ⚠️ Observações

- O aluno só pode visualizar vantagens, não pode editá-las
- O sistema valida automaticamente a autenticação via JWT
- Todas as operações requerem token de autenticação válido
- Os dados são perdidos quando o backend é reiniciado (banco H2 em memória)
- Ao resgatar uma vantagem, o saldo é automaticamente descontado
- Cada resgate gera um código de cupom único para validação

---

## 🎨 Interface

A página de vantagens para alunos apresenta:
- **Cards visuais** com imagens das vantagens
- **Saldo destacado** no topo da página
- **Indicador visual** se o aluno tem saldo suficiente para resgatar
- **Modal de imagem** para visualizar fotos em tamanho maior
- **Design responsivo** que funciona em diferentes tamanhos de tela

