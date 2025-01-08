-- ************************************************************
-- SCRIPT 1: Criação do Banco de Dados "Livraria"
-- ************************************************************

-- Este script cria o banco de dados "Livraria". 
-- Após executar este script, o banco de dados será criado,
-- mas você precisará se conectar manualmente a ele para continuar.

-- Criação do banco de dados
CREATE DATABASE "Livraria"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE "Livraria"
    IS 'Base de dados criada para o cenário do recrutamento interno para a vaga de Programador Java Jr.';

-- ************************************************************
-- INSTRUÇÕES:
-- Após a execução deste script, o banco de dados "Livraria" será criado.
-- 
-- Passo 1: Abra o pgAdmin ou a linha de comando do PostgreSQL.
-- Passo 2: Conecte-se ao banco de dados "Livraria". 
-- No pgAdmin, clique em "Conectar" no banco de dados "Livraria".
-- No psql, use o comando:
--   psql -d Livraria
-- ************************************************************
