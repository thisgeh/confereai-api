-- ============================================================
-- ConfereAí - Script de referência do banco (SQL Server)
-- As tabelas são criadas automaticamente pelo Hibernate
-- (spring.jpa.hibernate.ddl-auto=update). Este script serve
-- como documentação do modelo e para criação manual, se preferir.
-- ============================================================

-- Local (container): crie o banco antes de subir a API
-- IF DB_ID('confereai') IS NULL CREATE DATABASE confereai;
-- GO
-- USE confereai;

CREATE TABLE usuarios (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome             VARCHAR(120) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    telefone         VARCHAR(20)  NOT NULL,
    data_nascimento  DATE NULL,
    data_cadastro    DATETIME2(6) NOT NULL
);

CREATE TABLE contatos_confianca (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    telefone    VARCHAR(20)  NOT NULL,
    parentesco  VARCHAR(50)  NULL,
    principal   BIT NOT NULL,
    usuario_id  BIGINT NOT NULL REFERENCES usuarios(id)
);

CREATE TABLE perguntas_golpe (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    texto       VARCHAR(255) NOT NULL,
    explicacao  VARCHAR(500) NOT NULL,
    peso        INT NOT NULL,
    categoria   VARCHAR(50) NULL,
    ativa       BIT NOT NULL
);

CREATE TABLE verificacoes (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    usuario_id          BIGINT NOT NULL REFERENCES usuarios(id),
    tipo_pagamento      VARCHAR(10) NOT NULL,
    cnpj                VARCHAR(14) NOT NULL,
    nome_informado      VARCHAR(150) NULL,
    codigo_banco        VARCHAR(3) NULL,
    valor               NUMERIC(15,2) NULL,
    razao_social        VARCHAR(200) NULL,
    nome_fantasia       VARCHAR(200) NULL,
    situacao_cadastral  VARCHAR(50) NULL,
    data_abertura       DATE NULL,
    nome_confere        BIT NULL,
    banco_nome          VARCHAR(200) NULL,
    pontuacao_risco     INT NOT NULL,
    resultado           VARCHAR(10) NOT NULL,
    alertas             VARCHAR(4000) NULL,
    data_verificacao    DATETIME2(6) NOT NULL
);

-- N:N - perguntas respondidas com "Sim" em cada verificação
CREATE TABLE verificacoes_sinais (
    verificacao_id  BIGINT NOT NULL REFERENCES verificacoes(id),
    pergunta_id     BIGINT NOT NULL REFERENCES perguntas_golpe(id),
    PRIMARY KEY (verificacao_id, pergunta_id)
);
