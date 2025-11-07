# 📋 Dados de Acesso - Empresa Parceira

## 🏢 Empresa Parceira Cadastrada

### Empresa Teste Parceira

**Dados de Login:**
- **Email:** `empresa@teste.com`
- **Senha:** `empresa123`
- **Tipo de Usuário:** EMPRESA

**Dados Cadastrais:**
- **Nome:** Empresa Teste Parceira
- **CNPJ:** 11.222.333/0001-44
- **Email de Contato:** contato@empresateste.com

---

## 🔐 Como Acessar

### 1. Via Frontend

1. Acesse: **http://localhost:3000/signin**
2. Faça login com:
   - Email: `empresa@teste.com`
   - Senha: `empresa123`
3. Após o login, acesse: **http://localhost:3000/empresa/vantagens**

### 2. Via API

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "empresa@teste.com",
    "senha": "empresa123"
  }'
```

---

## 📍 Funcionalidades Disponíveis

Após fazer login como empresa parceira, você pode:

- ✅ **Cadastrar Vantagens** - Adicionar novos produtos/serviços
- ✅ **Editar Vantagens** - Atualizar informações das vantagens cadastradas
- ✅ **Excluir Vantagens** - Remover vantagens do sistema
- ✅ **Visualizar Vantagens** - Ver todas as vantagens da sua empresa

---

## 🎯 Endpoints da API

### Criar Vantagem
```bash
POST http://localhost:8080/api/vantagens
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Desconto de 15% em Notebooks",
  "descricao": "Ganhe 15% de desconto na compra de notebooks selecionados",
  "fotoUrl": "https://exemplo.com/notebook.jpg",
  "custoEmMoedas": 350.0
}
```

### Listar Minhas Vantagens
```bash
GET http://localhost:8080/api/vantagens/minhas
Authorization: Bearer {token}
```

### Atualizar Vantagem
```bash
PUT http://localhost:8080/api/vantagens/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Desconto Atualizado",
  "descricao": "Nova descrição",
  "fotoUrl": "https://exemplo.com/nova-foto.jpg",
  "custoEmMoedas": 400.0
}
```

### Deletar Vantagem
```bash
DELETE http://localhost:8080/api/vantagens/{id}
Authorization: Bearer {token}
```

---

## 📝 Exemplo de Vantagem

Ao cadastrar uma vantagem, você precisa informar:

- **Nome** (obrigatório) - Nome da vantagem
- **Descrição** (opcional) - Descrição detalhada (máx. 1000 caracteres)
- **URL da Foto** (opcional) - Link para imagem do produto
- **Custo em Moedas** (obrigatório) - Quantidade de moedas necessárias (deve ser > 0)

---

## ⚠️ Observações

- A empresa só pode gerenciar suas próprias vantagens
- O sistema valida automaticamente a autenticação via JWT
- Todas as operações requerem token de autenticação válido
- Os dados são perdidos quando o backend é reiniciado (banco H2 em memória)

---

## 🔄 Outras Empresas Cadastradas

O sistema também possui outras empresas de exemplo:

1. **Tech Store**
   - Email: `contato@techstore.com.br`
   - Senha: `emp123`

2. **Livraria Online**
   - Email: `contato@livrariaonline.com.br`
   - Senha: `emp456`

