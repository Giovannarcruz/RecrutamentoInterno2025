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

-- Conectando no banco de dados Livraria
\c "Livraria"

-- Criação da tabela de gêneros
CREATE TABLE IF NOT EXISTS public.generos
(
    id serial PRIMARY KEY,
    nome character varying(50) NOT NULL UNIQUE
);

-- Criação da tabela de livros
CREATE TABLE IF NOT EXISTS public.livros
(
    etiqueta_livro smallint PRIMARY KEY,
    titulo character varying(80),
    autor character varying(80),
    editora character varying(50),
    genero character varying(50),
    isbn character varying(13) UNIQUE,
    data_publicacao date,
    data_inclusao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    data_alteracao timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

-- Função para atualizar a data de inclusão
CREATE OR REPLACE FUNCTION public.fn_atualizar_data_inclusao() 
RETURNS trigger AS $$
BEGIN
    NEW.data_inclusao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Função para atualizar a data de alteração
CREATE OR REPLACE FUNCTION public.fn_atualizar_data_alteracao() 
RETURNS trigger AS $$
BEGIN
    NEW.data_alteracao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Função para gerar a etiqueta do livro
CREATE OR REPLACE FUNCTION public.fn_gerar_etiqueta() 
RETURNS trigger AS $$
BEGIN
    NEW.etiqueta_livro = nextval('livros_etiqueta_livro_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Criação dos triggers
CREATE TRIGGER trg_data_alteracao
    BEFORE UPDATE ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_atualizar_data_alteracao();

CREATE TRIGGER trg_data_inclusao
    BEFORE INSERT ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_atualizar_data_inclusao();

CREATE TRIGGER trg_gerar_etiqueta
    BEFORE INSERT ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_gerar_etiqueta();

-- Criação da tabela de livros semelhantes
CREATE TABLE IF NOT EXISTS public.livros_semelhantes
(
    etiqueta_livro integer NOT NULL,
    etiqueta_semelhante integer NOT NULL,
    PRIMARY KEY (etiqueta_livro, etiqueta_semelhante),
    FOREIGN KEY (etiqueta_livro) REFERENCES public.livros (etiqueta_livro) ON DELETE CASCADE,
    FOREIGN KEY (etiqueta_semelhante) REFERENCES public.livros (etiqueta_livro) ON DELETE CASCADE
);

-- Ajustar propriedade de ownership das tabelas
ALTER TABLE IF EXISTS public.generos OWNER TO postgres;
ALTER TABLE IF EXISTS public.livros OWNER TO postgres;
ALTER TABLE IF EXISTS public.livros_semelhantes OWNER TO postgres;
