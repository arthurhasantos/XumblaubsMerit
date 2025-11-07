# 📊 Tabela de Resgates no Banco de Dados

## 🗄️ Estrutura da Tabela `resgate_vantagem`

A tabela `resgate_vantagem` armazena todos os resgates de vantagens realizados pelos alunos.

### Estrutura da Tabela

```sql
CREATE TABLE resgate_vantagem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_resgate TIMESTAMP NOT NULL,
    codigo_cupom VARCHAR(255) NOT NULL UNIQUE,
    fk_aluno BIGINT NOT NULL,
    fk_vantagem BIGINT NOT NULL,
    FOREIGN KEY (fk_aluno) REFERENCES aluno(id),
    FOREIGN KEY (fk_vantagem) REFERENCES vantagem(id)
);
```

### Campos

- **id**: Identificador único do resgate (chave primária, auto-incremento)
- **data_resgate**: Data e hora do resgate (preenchida automaticamente)
- **codigo_cupom**: Código único do cupom gerado (formato: `CUPOM-XXXX-XXXX-XXXX`)
- **fk_aluno**: ID do aluno que realizou o resgate (chave estrangeira)
- **fk_vantagem**: ID da vantagem resgatada (chave estrangeira)

---

## 📋 Consultas Úteis

### 1. Listar Todos os Resgates

```sql
SELECT * FROM resgate_vantagem;
```

### 2. Listar Resgates com Informações do Aluno e Vantagem

```sql
SELECT 
    r.id,
    r.data_resgate,
    r.codigo_cupom,
    a.nome AS aluno_nome,
    a.email AS aluno_email,
    v.nome AS vantagem_nome,
    v.custo_em_moedas,
    e.nome AS empresa_nome
FROM resgate_vantagem r
JOIN aluno a ON r.fk_aluno = a.id
JOIN vantagem v ON r.fk_vantagem = v.id
JOIN empresa_parceira e ON v.fk_empresa = e.id
ORDER BY r.data_resgate DESC;
```

### 3. Resgates de um Aluno Específico

```sql
SELECT 
    r.id,
    r.data_resgate,
    r.codigo_cupom,
    v.nome AS vantagem_nome,
    v.custo_em_moedas
FROM resgate_vantagem r
JOIN aluno a ON r.fk_aluno = a.id
JOIN vantagem v ON r.fk_vantagem = v.id
WHERE a.email = 'joao.silva@aluno.pucminas.br'
ORDER BY r.data_resgate DESC;
```

### 4. Buscar Resgate por Código de Cupom

```sql
SELECT 
    r.*,
    a.nome AS aluno_nome,
    v.nome AS vantagem_nome
FROM resgate_vantagem r
JOIN aluno a ON r.fk_aluno = a.id
JOIN vantagem v ON r.fk_vantagem = v.id
WHERE r.codigo_cupom = 'CUPOM-XXXX-XXXX-XXXX';
```

### 5. Resgates por Período

```sql
SELECT 
    r.*,
    a.nome AS aluno_nome,
    v.nome AS vantagem_nome
FROM resgate_vantagem r
JOIN aluno a ON r.fk_aluno = a.id
JOIN vantagem v ON r.fk_vantagem = v.id
WHERE r.data_resgate BETWEEN '2024-01-01' AND '2024-12-31'
ORDER BY r.data_resgate DESC;
```

### 6. Resgates por Empresa

```sql
SELECT 
    r.*,
    a.nome AS aluno_nome,
    v.nome AS vantagem_nome,
    e.nome AS empresa_nome
FROM resgate_vantagem r
JOIN aluno a ON r.fk_aluno = a.id
JOIN vantagem v ON r.fk_vantagem = v.id
JOIN empresa_parceira e ON v.fk_empresa = e.id
WHERE e.id = 1
ORDER BY r.data_resgate DESC;
```

### 7. Total de Moedas Gastas por Aluno

```sql
SELECT 
    a.nome AS aluno_nome,
    a.email,
    COUNT(r.id) AS total_resgates,
    SUM(v.custo_em_moedas) AS total_moedas_gastas
FROM aluno a
LEFT JOIN resgate_vantagem r ON a.id = r.fk_aluno
LEFT JOIN vantagem v ON r.fk_vantagem = v.id
GROUP BY a.id, a.nome, a.email
ORDER BY total_moedas_gastas DESC;
```

### 8. Vantagens Mais Resgatadas

```sql
SELECT 
    v.nome AS vantagem_nome,
    COUNT(r.id) AS total_resgates,
    SUM(v.custo_em_moedas) AS total_moedas_recebidas
FROM vantagem v
LEFT JOIN resgate_vantagem r ON v.id = r.fk_vantagem
GROUP BY v.id, v.nome
ORDER BY total_resgates DESC;
```

---

## 🔍 Como Acessar o Banco de Dados

1. Com o backend rodando, acesse: **http://localhost:8080/h2-console**

2. Preencha os campos:
   - **JDBC URL:** `jdbc:h2:mem:meritodb`
   - **User Name:** `sa`
   - **Password:** (deixe em branco)

3. Clique em **Connect**

4. Execute as queries SQL acima para visualizar os resgates

---

## 📊 Exemplo de Dados

Após alguns resgates, a tabela pode ter dados como:

| id | data_resgate | codigo_cupom | fk_aluno | fk_vantagem |
|----|--------------|--------------|----------|-------------|
| 1 | 2024-01-15 10:30:00 | CUPOM-A1B2-C3D4-E5F6 | 1 | 1 |
| 2 | 2024-01-16 14:20:00 | CUPOM-X9Y8-Z7W6-V5U4 | 1 | 2 |
| 3 | 2024-01-17 09:15:00 | CUPOM-M1N2-O3P4-Q5R6 | 2 | 3 |

---

## ⚠️ Observações

- Cada resgate gera um código de cupom único
- A data do resgate é preenchida automaticamente pelo sistema
- Os resgates são vinculados ao aluno e à vantagem através de chaves estrangeiras
- A tabela mantém o histórico completo de todos os resgates realizados
- Os dados são perdidos quando o backend é reiniciado (banco H2 em memória)

