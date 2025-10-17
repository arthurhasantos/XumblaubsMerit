-- ═══════════════════════════════════════════════════════════════════════════════
-- CONSULTAS SQL DE EXEMPLO - Sistema de Mérito Acadêmico
-- Execute estas queries no H2 Console para testar o sistema
-- URL: http://localhost:8080/h2-console
-- ═══════════════════════════════════════════════════════════════════════════════

-- ───────────────────────────────────────────────────────────────────────────────
-- 1. VISUALIZAR ESTRUTURA DAS TABELAS
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todas as tabelas criadas
SHOW TABLES;

-- Ver estrutura da tabela USUARIO
SHOW COLUMNS FROM USUARIO;

-- Ver estrutura da tabela ALUNO
SHOW COLUMNS FROM ALUNO;

-- ───────────────────────────────────────────────────────────────────────────────
-- 2. CONSULTAS BÁSICAS - INSTITUIÇÕES
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todas as instituições
SELECT * FROM INSTITUICAO;

-- Buscar instituição específica
SELECT * FROM INSTITUICAO WHERE NOME = 'PUC Minas';

-- ───────────────────────────────────────────────────────────────────────────────
-- 3. CONSULTAS BÁSICAS - USUÁRIOS (Tabela Base)
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todos os usuários (tabela base)
SELECT * FROM USUARIO;

-- Contar usuários por tipo
SELECT TIPO_USUARIO, COUNT(*) as TOTAL
FROM USUARIO
GROUP BY TIPO_USUARIO;

-- ───────────────────────────────────────────────────────────────────────────────
-- 4. CONSULTAS - ALUNOS
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todos os alunos
SELECT * FROM ALUNO;

-- Alunos com suas instituições
SELECT 
    a.NOME as ALUNO,
    a.CPF,
    a.CURSO,
    a.SALDO_MOEDAS,
    i.NOME as INSTITUICAO
FROM ALUNO a
JOIN INSTITUICAO i ON a.FK_INSTITUICAO = i.ID
ORDER BY a.SALDO_MOEDAS DESC;

-- Alunos com saldo acima de 500 moedas
SELECT NOME, CURSO, SALDO_MOEDAS
FROM ALUNO
WHERE SALDO_MOEDAS > 500
ORDER BY SALDO_MOEDAS DESC;

-- Total de moedas por curso
SELECT 
    CURSO,
    COUNT(*) as TOTAL_ALUNOS,
    SUM(SALDO_MOEDAS) as TOTAL_MOEDAS,
    AVG(SALDO_MOEDAS) as MEDIA_MOEDAS
FROM ALUNO
GROUP BY CURSO;

-- ───────────────────────────────────────────────────────────────────────────────
-- 5. CONSULTAS - PROFESSORES
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todos os professores
SELECT * FROM PROFESSOR;

-- Professores com suas instituições
SELECT 
    p.NOME as PROFESSOR,
    p.CPF,
    p.DEPARTAMENTO,
    p.SALDO_MOEDAS,
    i.NOME as INSTITUICAO
FROM PROFESSOR p
JOIN INSTITUICAO i ON p.FK_INSTITUICAO = i.ID;

-- Professores por departamento
SELECT DEPARTAMENTO, COUNT(*) as TOTAL
FROM PROFESSOR
GROUP BY DEPARTAMENTO;

-- ───────────────────────────────────────────────────────────────────────────────
-- 6. CONSULTAS - EMPRESAS PARCEIRAS
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todas as empresas
SELECT * FROM EMPRESA_PARCEIRA;

-- Empresas com quantidade de vantagens
SELECT 
    e.NOME as EMPRESA,
    e.CNPJ,
    COUNT(v.ID) as TOTAL_VANTAGENS
FROM EMPRESA_PARCEIRA e
LEFT JOIN VANTAGEM v ON v.FK_EMPRESA = e.ID
GROUP BY e.ID, e.NOME, e.CNPJ;

-- ───────────────────────────────────────────────────────────────────────────────
-- 7. CONSULTAS - VANTAGENS
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todas as vantagens
SELECT * FROM VANTAGEM;

-- Vantagens com suas empresas
SELECT 
    v.NOME as VANTAGEM,
    v.DESCRICAO,
    v.CUSTO_EM_MOEDAS,
    e.NOME as EMPRESA
FROM VANTAGEM v
JOIN EMPRESA_PARCEIRA e ON v.FK_EMPRESA = e.ID
ORDER BY v.CUSTO_EM_MOEDAS;

-- Vantagens mais baratas (até 500 moedas)
SELECT NOME, CUSTO_EM_MOEDAS, DESCRICAO
FROM VANTAGEM
WHERE CUSTO_EM_MOEDAS <= 500
ORDER BY CUSTO_EM_MOEDAS;

-- Vantagens mais caras
SELECT NOME, CUSTO_EM_MOEDAS, DESCRICAO
FROM VANTAGEM
ORDER BY CUSTO_EM_MOEDAS DESC
LIMIT 3;

-- Estatísticas de vantagens
SELECT 
    COUNT(*) as TOTAL_VANTAGENS,
    MIN(CUSTO_EM_MOEDAS) as MENOR_CUSTO,
    MAX(CUSTO_EM_MOEDAS) as MAIOR_CUSTO,
    AVG(CUSTO_EM_MOEDAS) as CUSTO_MEDIO
FROM VANTAGEM;

-- ───────────────────────────────────────────────────────────────────────────────
-- 8. CONSULTAS - RESGATES
-- ───────────────────────────────────────────────────────────────────────────────

-- Listar todos os resgates
SELECT * FROM RESGATE_VANTAGEM;

-- Resgates detalhados (com aluno e vantagem)
SELECT 
    r.DATA_RESGATE,
    r.CODIGO_CUPOM,
    a.NOME as ALUNO,
    v.NOME as VANTAGEM,
    v.CUSTO_EM_MOEDAS,
    e.NOME as EMPRESA
FROM RESGATE_VANTAGEM r
JOIN ALUNO a ON r.FK_ALUNO = a.ID
JOIN VANTAGEM v ON r.FK_VANTAGEM = v.ID
JOIN EMPRESA_PARCEIRA e ON v.FK_EMPRESA = e.ID
ORDER BY r.DATA_RESGATE DESC;

-- Histórico de resgates de um aluno específico
SELECT 
    r.DATA_RESGATE,
    r.CODIGO_CUPOM,
    v.NOME as VANTAGEM,
    v.CUSTO_EM_MOEDAS
FROM RESGATE_VANTAGEM r
JOIN VANTAGEM v ON r.FK_VANTAGEM = v.ID
JOIN ALUNO a ON r.FK_ALUNO = a.ID
WHERE a.CPF = '111.222.333-44'
ORDER BY r.DATA_RESGATE DESC;

-- Total de resgates por aluno
SELECT 
    a.NOME as ALUNO,
    COUNT(r.ID) as TOTAL_RESGATES,
    SUM(v.CUSTO_EM_MOEDAS) as TOTAL_GASTO
FROM ALUNO a
LEFT JOIN RESGATE_VANTAGEM r ON r.FK_ALUNO = a.ID
LEFT JOIN VANTAGEM v ON r.FK_VANTAGEM = v.ID
GROUP BY a.ID, a.NOME
ORDER BY TOTAL_RESGATES DESC;

-- Vantagens mais resgatadas
SELECT 
    v.NOME as VANTAGEM,
    COUNT(r.ID) as TOTAL_RESGATES,
    e.NOME as EMPRESA
FROM VANTAGEM v
LEFT JOIN RESGATE_VANTAGEM r ON r.FK_VANTAGEM = v.ID
JOIN EMPRESA_PARCEIRA e ON v.FK_EMPRESA = e.ID
GROUP BY v.ID, v.NOME, e.NOME
ORDER BY TOTAL_RESGATES DESC;

-- ───────────────────────────────────────────────────────────────────────────────
-- 9. CONSULTAS COMPLEXAS - JOINS MÚLTIPLOS
-- ───────────────────────────────────────────────────────────────────────────────

-- Visão completa: Aluno, Instituição, Resgates
SELECT 
    a.NOME as ALUNO,
    a.CURSO,
    i.NOME as INSTITUICAO,
    a.SALDO_MOEDAS,
    COUNT(r.ID) as TOTAL_RESGATES
FROM ALUNO a
JOIN INSTITUICAO i ON a.FK_INSTITUICAO = i.ID
LEFT JOIN RESGATE_VANTAGEM r ON r.FK_ALUNO = a.ID
GROUP BY a.ID, a.NOME, a.CURSO, i.NOME, a.SALDO_MOEDAS;

-- Análise por instituição
SELECT 
    i.NOME as INSTITUICAO,
    COUNT(DISTINCT a.ID) as TOTAL_ALUNOS,
    COUNT(DISTINCT p.ID) as TOTAL_PROFESSORES,
    SUM(a.SALDO_MOEDAS) as TOTAL_MOEDAS_ALUNOS,
    SUM(p.SALDO_MOEDAS) as TOTAL_MOEDAS_PROFESSORES
FROM INSTITUICAO i
LEFT JOIN ALUNO a ON a.FK_INSTITUICAO = i.ID
LEFT JOIN PROFESSOR p ON p.FK_INSTITUICAO = i.ID
GROUP BY i.ID, i.NOME;

-- ───────────────────────────────────────────────────────────────────────────────
-- 10. CONSULTAS AVANÇADAS - RELATÓRIOS
-- ───────────────────────────────────────────────────────────────────────────────

-- Ranking de alunos por saldo de moedas
SELECT 
    ROW_NUMBER() OVER (ORDER BY SALDO_MOEDAS DESC) as RANKING,
    NOME,
    CURSO,
    SALDO_MOEDAS
FROM ALUNO
ORDER BY SALDO_MOEDAS DESC;

-- Alunos que podem comprar cada vantagem
SELECT 
    v.NOME as VANTAGEM,
    v.CUSTO_EM_MOEDAS,
    COUNT(a.ID) as ALUNOS_PODEM_COMPRAR
FROM VANTAGEM v
LEFT JOIN ALUNO a ON a.SALDO_MOEDAS >= v.CUSTO_EM_MOEDAS
GROUP BY v.ID, v.NOME, v.CUSTO_EM_MOEDAS
ORDER BY v.CUSTO_EM_MOEDAS;

-- Vantagens disponíveis para cada aluno
SELECT 
    a.NOME as ALUNO,
    a.SALDO_MOEDAS,
    v.NOME as VANTAGEM_DISPONIVEL,
    v.CUSTO_EM_MOEDAS
FROM ALUNO a
CROSS JOIN VANTAGEM v
WHERE a.SALDO_MOEDAS >= v.CUSTO_EM_MOEDAS
ORDER BY a.NOME, v.CUSTO_EM_MOEDAS;

-- ───────────────────────────────────────────────────────────────────────────────
-- 11. CONSULTAS DE VERIFICAÇÃO - HERANÇA
-- ───────────────────────────────────────────────────────────────────────────────

-- Verificar herança JOINED: Usuários e suas especializações
SELECT 
    u.ID,
    u.EMAIL,
    u.TIPO_USUARIO,
    CASE 
        WHEN a.ID IS NOT NULL THEN a.NOME
        WHEN p.ID IS NOT NULL THEN p.NOME
        WHEN e.ID IS NOT NULL THEN e.NOME
    END as NOME
FROM USUARIO u
LEFT JOIN ALUNO a ON u.ID = a.ID
LEFT JOIN PROFESSOR p ON u.ID = p.ID
LEFT JOIN EMPRESA_PARCEIRA e ON u.ID = e.ID;

-- Contar registros em cada tabela
SELECT 'USUARIO' as TABELA, COUNT(*) as TOTAL FROM USUARIO
UNION ALL
SELECT 'ALUNO', COUNT(*) FROM ALUNO
UNION ALL
SELECT 'PROFESSOR', COUNT(*) FROM PROFESSOR
UNION ALL
SELECT 'EMPRESA_PARCEIRA', COUNT(*) FROM EMPRESA_PARCEIRA
UNION ALL
SELECT 'INSTITUICAO', COUNT(*) FROM INSTITUICAO
UNION ALL
SELECT 'VANTAGEM', COUNT(*) FROM VANTAGEM
UNION ALL
SELECT 'RESGATE_VANTAGEM', COUNT(*) FROM RESGATE_VANTAGEM;

-- ───────────────────────────────────────────────────────────────────────────────
-- 12. CONSULTAS DE NEGÓCIO
-- ───────────────────────────────────────────────────────────────────────────────

-- Alunos com saldo insuficiente para qualquer vantagem
SELECT NOME, SALDO_MOEDAS
FROM ALUNO
WHERE SALDO_MOEDAS < (SELECT MIN(CUSTO_EM_MOEDAS) FROM VANTAGEM);

-- Vantagens nunca resgatadas
SELECT v.NOME, v.CUSTO_EM_MOEDAS, e.NOME as EMPRESA
FROM VANTAGEM v
JOIN EMPRESA_PARCEIRA e ON v.FK_EMPRESA = e.ID
WHERE v.ID NOT IN (SELECT DISTINCT FK_VANTAGEM FROM RESGATE_VANTAGEM);

-- Empresas mais populares (mais resgates)
SELECT 
    e.NOME as EMPRESA,
    COUNT(r.ID) as TOTAL_RESGATES
FROM EMPRESA_PARCEIRA e
JOIN VANTAGEM v ON v.FK_EMPRESA = e.ID
LEFT JOIN RESGATE_VANTAGEM r ON r.FK_VANTAGEM = v.ID
GROUP BY e.ID, e.NOME
ORDER BY TOTAL_RESGATES DESC;

-- ───────────────────────────────────────────────────────────────────────────────
-- 13. EXEMPLOS DE INSERÇÃO (SE QUISER ADICIONAR MAIS DADOS)
-- ───────────────────────────────────────────────────────────────────────────────

/*
-- Inserir nova instituição
INSERT INTO INSTITUICAO (NOME, ENDERECO) 
VALUES ('CEFET-MG', 'Av. Amazonas, 7675 - Nova Gameleira, Belo Horizonte - MG');

-- Inserir novo usuário (para depois criar Aluno)
INSERT INTO USUARIO (EMAIL, SENHA, TIPO_USUARIO) 
VALUES ('pedro.souza@aluno.pucminas.br', 'senha789', 'ALUNO');

-- Inserir novo aluno (usar o ID gerado acima)
INSERT INTO ALUNO (ID, NOME, CPF, RG, ENDERECO, CURSO, SALDO_MOEDAS, FK_INSTITUICAO) 
VALUES (
    (SELECT ID FROM USUARIO WHERE EMAIL = 'pedro.souza@aluno.pucminas.br'),
    'Pedro Souza',
    '777.888.999-00',
    'MG-11.222.333',
    'Rua C, 789 - Belo Horizonte',
    'Sistemas de Informação',
    750.0,
    (SELECT ID FROM INSTITUICAO WHERE NOME = 'PUC Minas')
);
*/

-- ───────────────────────────────────────────────────────────────────────────────
-- 14. CONSULTAS DE ANÁLISE - DASHBOARDS
-- ───────────────────────────────────────────────────────────────────────────────

-- Dashboard Geral
SELECT 
    (SELECT COUNT(*) FROM INSTITUICAO) as TOTAL_INSTITUICOES,
    (SELECT COUNT(*) FROM ALUNO) as TOTAL_ALUNOS,
    (SELECT COUNT(*) FROM PROFESSOR) as TOTAL_PROFESSORES,
    (SELECT COUNT(*) FROM EMPRESA_PARCEIRA) as TOTAL_EMPRESAS,
    (SELECT COUNT(*) FROM VANTAGEM) as TOTAL_VANTAGENS,
    (SELECT COUNT(*) FROM RESGATE_VANTAGEM) as TOTAL_RESGATES,
    (SELECT SUM(SALDO_MOEDAS) FROM ALUNO) as TOTAL_MOEDAS_CIRCULANDO;

-- Top 5 Alunos mais ricos
SELECT NOME, CURSO, SALDO_MOEDAS
FROM ALUNO
ORDER BY SALDO_MOEDAS DESC
LIMIT 5;

-- Top 3 Vantagens mais caras
SELECT NOME, CUSTO_EM_MOEDAS, DESCRICAO
FROM VANTAGEM
ORDER BY CUSTO_EM_MOEDAS DESC
LIMIT 3;

-- ═══════════════════════════════════════════════════════════════════════════════
-- FIM DAS CONSULTAS DE EXEMPLO
-- ═══════════════════════════════════════════════════════════════════════════════

-- Dica: Para visualizar dados de forma mais amigável no H2 Console:
-- 1. Execute as queries acima
-- 2. Clique em "Run" ou pressione Ctrl+Enter
-- 3. Os resultados aparecerão na parte inferior da tela
-- 4. Você pode exportar os resultados em CSV, se necessário

-- ═══════════════════════════════════════════════════════════════════════════════

