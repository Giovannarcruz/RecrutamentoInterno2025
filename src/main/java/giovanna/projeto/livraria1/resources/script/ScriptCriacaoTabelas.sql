-- ************************************************************
-- SCRIPT 2: Criação das Tabelas, Funções e Triggers no banco "Livraria"
-- ************************************************************

-- INSTRUÇÕES:
-- 1. Antes de rodar este script, certifique-se de estar conectado ao banco de dados "Livraria".
--    Se não estiver conectado, siga o próximo passo:
--    - No pgAdmin, clique no banco de dados "Livraria" na árvore de objetos e clique em "Conectar".
-- 2. Após a conexão, execute este script para criar as tabelas, funções e triggers no banco de dados.

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

-- ************************************************************
-- INSTRUÇÕES FINAIS:
-- 1. Após conectar-se ao banco "Livraria" no pgAdmin, abra este script no painel de consultas.
-- 2. Clique em "Executar" para rodar o script e criar as tabelas e funções necessárias.
-- ************************************************************
