-- ===============================================
-- SCHEMA DO BANCO DE DADOS - Sistema de Mérito Acadêmico
-- Este arquivo documenta o schema que será criado automaticamente pelo Hibernate
-- ===============================================

-- Tabela: INSTITUICAO
CREATE TABLE instituicao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL
);

-- Tabela: USUARIO (Tabela base para herança)
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(255) NOT NULL
);

-- Tabela: ALUNO (Herança de Usuario)
CREATE TABLE aluno (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    rg VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    curso VARCHAR(255) NOT NULL,
    saldo_moedas DOUBLE NOT NULL DEFAULT 0.0,
    fk_instituicao BIGINT NOT NULL,
    FOREIGN KEY (id) REFERENCES usuario(id),
    FOREIGN KEY (fk_instituicao) REFERENCES instituicao(id)
);

-- Tabela: PROFESSOR (Herança de Usuario)
CREATE TABLE professor (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    departamento VARCHAR(255) NOT NULL,
    saldo_moedas DOUBLE NOT NULL DEFAULT 0.0,
    fk_instituicao BIGINT NOT NULL,
    FOREIGN KEY (id) REFERENCES usuario(id),
    FOREIGN KEY (fk_instituicao) REFERENCES instituicao(id)
);

-- Tabela: EMPRESA_PARCEIRA (Herança de Usuario)
CREATE TABLE empresa_parceira (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) NOT NULL UNIQUE,
    email_contato VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES usuario(id)
);

-- Tabela: VANTAGEM
CREATE TABLE vantagem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    foto_url VARCHAR(255),
    custo_em_moedas DOUBLE NOT NULL,
    fk_empresa BIGINT NOT NULL,
    FOREIGN KEY (fk_empresa) REFERENCES empresa_parceira(id)
);

-- Tabela: RESGATE_VANTAGEM
CREATE TABLE resgate_vantagem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_resgate TIMESTAMP NOT NULL,
    codigo_cupom VARCHAR(255) NOT NULL UNIQUE,
    fk_aluno BIGINT NOT NULL,
    fk_vantagem BIGINT NOT NULL,
    FOREIGN KEY (fk_aluno) REFERENCES aluno(id),
    FOREIGN KEY (fk_vantagem) REFERENCES vantagem(id)
);

-- ===============================================
-- ÍNDICES ADICIONAIS (Opcionais - melhoram performance)
-- ===============================================

-- Índices para busca por campos únicos
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_aluno_cpf ON aluno(cpf);
CREATE INDEX idx_professor_cpf ON professor(cpf);
CREATE INDEX idx_empresa_cnpj ON empresa_parceira(cnpj);
CREATE INDEX idx_resgate_cupom ON resgate_vantagem(codigo_cupom);

-- Índices para chaves estrangeiras (melhoram JOINs)
CREATE INDEX idx_aluno_instituicao ON aluno(fk_instituicao);
CREATE INDEX idx_professor_instituicao ON professor(fk_instituicao);
CREATE INDEX idx_vantagem_empresa ON vantagem(fk_empresa);
CREATE INDEX idx_resgate_aluno ON resgate_vantagem(fk_aluno);
CREATE INDEX idx_resgate_vantagem ON resgate_vantagem(fk_vantagem);

-- Índice para busca por data de resgate
CREATE INDEX idx_resgate_data ON resgate_vantagem(data_resgate);

-- ===============================================
-- CONSULTAS ÚTEIS
-- ===============================================

-- Listar todos os alunos com suas instituições
-- SELECT a.nome, a.cpf, a.curso, a.saldo_moedas, i.nome as instituicao
-- FROM aluno a
-- JOIN instituicao i ON a.fk_instituicao = i.id;

-- Listar todas as vantagens com suas empresas
-- SELECT v.nome, v.descricao, v.custo_em_moedas, e.nome as empresa
-- FROM vantagem v
-- JOIN empresa_parceira e ON v.fk_empresa = e.id;

-- Listar resgates de um aluno específico
-- SELECT r.data_resgate, r.codigo_cupom, v.nome as vantagem, v.custo_em_moedas
-- FROM resgate_vantagem r
-- JOIN vantagem v ON r.fk_vantagem = v.id
-- JOIN aluno a ON r.fk_aluno = a.id
-- WHERE a.cpf = '111.222.333-44'
-- ORDER BY r.data_resgate DESC;

-- Ver saldo de moedas de todos os alunos
-- SELECT a.nome, a.curso, a.saldo_moedas, i.nome as instituicao
-- FROM aluno a
-- JOIN instituicao i ON a.fk_instituicao = i.id
-- ORDER BY a.saldo_moedas DESC;

-- Vantagens disponíveis para um determinado saldo
-- SELECT nome, descricao, custo_em_moedas
-- FROM vantagem
-- WHERE custo_em_moedas <= 500
-- ORDER BY custo_em_moedas ASC;

