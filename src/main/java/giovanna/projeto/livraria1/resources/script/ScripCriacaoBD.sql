-- Database: Livraria

-- DROP DATABASE IF EXISTS "Livraria";

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



-- Table: public.generos

-- DROP TABLE IF EXISTS public.generos;

CREATE TABLE IF NOT EXISTS public.generos
(
    id integer NOT NULL DEFAULT nextval('generos_id_seq'::regclass),
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT generos_pkey PRIMARY KEY (id),
    CONSTRAINT generos_nome_key UNIQUE (nome)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.generos
    OWNER to postgres;

    -- Table: public.livros

-- DROP TABLE IF EXISTS public.livros;

CREATE TABLE IF NOT EXISTS public.livros
(
    etiqueta_livro smallint NOT NULL,
    titulo character varying(80) COLLATE pg_catalog."default",
    autor character varying(80) COLLATE pg_catalog."default",
    editora character varying(50) COLLATE pg_catalog."default",
    genero character varying(50) COLLATE pg_catalog."default",
    isbn character varying(13) COLLATE pg_catalog."default",
    data_publicacao date,
    data_inclusao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    data_alteracao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT livro_pkey PRIMARY KEY (etiqueta_livro),
    CONSTRAINT livro_isbn_key UNIQUE (isbn)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.livros
    OWNER to postgres;

-- Trigger: trg_data_alteracao

-- DROP TRIGGER IF EXISTS trg_data_alteracao ON public.livros;

CREATE TRIGGER trg_data_alteracao
    BEFORE UPDATE 
    ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_atualizar_data_alteracao();

-- Trigger: trg_data_inclusao

-- DROP TRIGGER IF EXISTS trg_data_inclusao ON public.livros;

CREATE TRIGGER trg_data_inclusao
    BEFORE INSERT
    ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_atualizar_data_inclusao();

-- Trigger: trg_gerar_etiqueta

-- DROP TRIGGER IF EXISTS trg_gerar_etiqueta ON public.livros;

CREATE TRIGGER trg_gerar_etiqueta
    BEFORE INSERT
    ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.fn_gerar_etiqueta();



    -- Table: public.livros_semelhantes

-- DROP TABLE IF EXISTS public.livros_semelhantes;

CREATE TABLE IF NOT EXISTS public.livros_semelhantes
(
    etiqueta_livro integer NOT NULL,
    etiqueta_semelhante integer NOT NULL,
    CONSTRAINT livros_semelhantes_pkey PRIMARY KEY (etiqueta_livro, etiqueta_semelhante),
    CONSTRAINT livros_semelhantes_etiqueta_livro_fkey FOREIGN KEY (etiqueta_livro)
        REFERENCES public.livros (etiqueta_livro) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT livros_semelhantes_etiqueta_semelhante_fkey FOREIGN KEY (etiqueta_semelhante)
        REFERENCES public.livros (etiqueta_livro) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.livros_semelhantes
    OWNER to postgres;