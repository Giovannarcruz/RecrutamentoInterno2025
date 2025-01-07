package giovanna.projeto.livraria1.services;

import giovanna.projeto.livraria1.dao.LivroDAO;
import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.util.ConnectionFactory;
import giovanna.projeto.livraria1.util.ISBNApiClient;

import javax.xml.rpc.ServiceException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serviço para manipulação de livros.
 */
public class LivroService {
    private static final Logger LOGGER = Logger.getLogger(LivroService.class.getName());
    private final LivroDAO livroDAO;

    /**
     *
     */
    public Livro busca_porEtiquetaetiquetaLivro;

    /**
     *
     */
    public LivroService() {
        this.livroDAO = new LivroDAO();
    }

    /**
     * Valida as informações de um livro.
     */
    private void validarLivro(Livro livro) throws ServiceException {
        LOGGER.info("Iniciando validação do livro...");
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new ServiceException("Título é obrigatório.");
        }
        if (livro.getIsbn() == null || livro.getIsbn().length() != 13) {
            throw new ServiceException("ISBN deve conter 13 caracteres.");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new ServiceException("Autor é obrigatório.");
        }
        if (livro.getEditora() == null || livro.getEditora().isBlank()) {
            throw new ServiceException("Editora é obrigatória.");
        }
        LOGGER.info("Validação concluída com sucesso.");
    }

    /**
     * Cadastra um novo livro.
     * @param livro
     * @return 
     * @throws javax.xml.rpc.ServiceException
     */
    public Livro cadastrarLivro(Livro livro) throws ServiceException {
        try {
            validarLivro(livro);
            livroDAO.inserirLivro(livro);
            LOGGER.info("Livro cadastrado com sucesso: " + livro.getTitulo());
            return livroDAO.busca_porISBN(livro.getIsbn()); // Retorna o livro com a etiqueta
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar livro", ex);
            throw new ServiceException("Erro ao cadastrar livro: " + ex.getMessage(), ex);
        }
    }

    /**
     * Atualiza um livro existente.
     * @param livro
     * @throws javax.xml.rpc.ServiceException
     */
    public void atualizaLivro(Livro livro) throws ServiceException {
         try {
        validarLivro(livro);
        if (livro.getEtiqueta_livro() <= 0) {
            throw new ServiceException("Etiqueta de livro inválida para atualização.");
        }
        livroDAO.alterarLivro(livro);
        LOGGER.info("Livro atualizado com sucesso: " + livro.getTitulo());
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao atualizar livro", ex);
        throw new ServiceException("Erro ao atualizar livro: " + ex.getMessage(), ex);
    }
    }

    /**
     * Cadastra um livro a partir de um ISBN utilizando uma API externa.
     * @param isbn o isbn-13 do livro
     * @throws javax.xml.rpc.ServiceException
     */
public void cadastrarLivroISBN(String isbn) throws ServiceException {
    try {
        LOGGER.info("Iniciando cadastro de livro via ISBN: " + isbn);

        Livro livroExistente = livroDAO.busca_porISBN(isbn); // Tenta buscar o livro

        if (livroExistente != null) { // Se encontrou, atualiza
            LOGGER.info("Livro com este ISBN já está cadastrado. Tentando atualização...");
            livroExistente.setData_alteracao(Date.valueOf(LocalDate.now()));
            atualizaLivro(livroExistente);
            return; // Sai do método após a atualização
        }

        // Se livroExistente == null, continua para o cadastro normal:
        Livro novoLivro = ISBNApiClient.buscarLivroPorISBN(isbn);
        if (novoLivro == null) {
            throw new ServiceException("Livro não encontrado na API para ISBN: " + isbn);
        }

        LOGGER.info("Validando o novo livro retornado pela API...");
        validarLivro(novoLivro);

        cadastrarLivro(novoLivro); // Cadastra o novo livro. O retorno já é tratado no salvarLivro do Dialog
        LOGGER.info("Livro cadastrado com sucesso: " + novoLivro.getTitulo());

    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao cadastrar livro por ISBN: " + isbn, ex);
        throw new ServiceException("Erro ao cadastrar livro por ISBN: " + ex.getMessage(), ex);
    }

    }

    /**
     * Exclui um livro pelo seu identificador.
     * @param etiqueta
     * @throws javax.xml.rpc.ServiceException
     */
    public void excluirLivro(int etiqueta) throws ServiceException {
        try {
            if (etiqueta <= 0) {
                throw new ServiceException("Etiqueta inválida.");
            }
            livroDAO.excluirLivro(etiqueta);
            LOGGER.info("Livro excluído com sucesso: " + etiqueta);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir livro", ex);
            throw new ServiceException("Erro ao excluir livro: " + ex.getMessage(), ex);
        }
    }

    /**
     * Busca um livro por ISBN.
     * @param isbn
     * @return 
     * @throws javax.xml.rpc.ServiceException
     */
    public Livro busca_porISBN(String isbn) throws ServiceException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = ConnectionFactory.getConnection(); // Conecta ao banco de dados

        String sql = "SELECT * FROM livros WHERE isbn = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, isbn);

        rs = stmt.executeQuery();

        if (rs.next()) {
            Livro livro = new Livro();
            livro.setEtiqueta_livro(rs.getInt("etiqueta_livro")); // Recupera a etiqueta
            livro.setIsbn(rs.getString("isbn"));
            livro.setTitulo(rs.getString("titulo"));
            livro.setAutor(rs.getString("autor"));
            livro.setEditora(rs.getString("editora"));
            livro.setGenero(rs.getString("genero"));
            livro.setData_publicacao(rs.getDate("data_publicacao").toLocalDate());

            return livro; // Retorna o livro encontrado
        }

        return null; // Retorna null se não encontrar o livro
    } catch (SQLException e) {
        throw new ServiceException("Erro ao buscar livro pelo ISBN", e);
    } finally {
        // Fechando recursos
        if (rs != null) try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
        if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
        if (conn != null) try { conn.close(); } catch (SQLException e) { /* Ignorar */ }
    }

    }

    /**
     * Lista todos os livros cadastrados.
     * @return 
     * @throws javax.xml.rpc.ServiceException
     */
    public List<Livro> lista_livros() throws ServiceException {
        try {
            return livroDAO.consultaLivros();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar livros", ex);
            throw new ServiceException("Erro ao listar livros: " + ex.getMessage(), ex);
        }
    }

    /**
     * Filtra livros com base nos critérios fornecidos.
     * @param titulo
     * @param autor
     * @param genero
     * @param isbn
     * @param dataPublicacao
     * @return 
     * @throws java.sql.SQLException 
     */
    public List<Livro> filtrarLivros(String titulo, String autor, String genero, String isbn, LocalDate dataPublicacao) throws SQLException {
        String sql = criarQueryFiltro(titulo, autor, genero, isbn, dataPublicacao);
        try (Connection connection= ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParametros(stmt, titulo, autor, genero, isbn, dataPublicacao);
            return executarConsulta(stmt);
        }
    }

    private String criarQueryFiltro(String titulo, String autor, String genero, String isbn, LocalDate dataPublicacao) {
        StringBuilder sql = new StringBuilder("SELECT * FROM livros WHERE 1=1");
        if (titulo != null) sql.append(" AND titulo LIKE ?");
        if (autor != null) sql.append(" AND autor LIKE ?");
        if (genero != null) sql.append(" AND genero LIKE ?");
        if (isbn != null) sql.append(" AND isbn LIKE ?");
        if (dataPublicacao != null) sql.append(" AND data_publicacao = ?");
        return sql.toString();
    }

    private void setParametros(PreparedStatement stmt, String titulo, String autor, String genero, String isbn, LocalDate dataPublicacao) throws SQLException {
        int paramIndex = 1;
        if (titulo != null) stmt.setString(paramIndex++, "%" + titulo + "%");
        if (autor != null) stmt.setString(paramIndex++, "%" + autor + "%");
        if (genero != null) stmt.setString(paramIndex++, "%" + genero + "%");
        if (isbn != null) stmt.setString(paramIndex++, "%" + isbn + "%");
        if (dataPublicacao != null) stmt.setDate(paramIndex++, Date.valueOf(dataPublicacao));
    }

    private List<Livro> executarConsulta(PreparedStatement stmt) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setEtiqueta_livro(rs.getInt("etiqueta_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setEditora(rs.getString("editora"));
                livro.setGenero(rs.getString("genero"));
                livro.setIsbn(rs.getString("isbn"));
                Date dataSQL = rs.getDate("data_publicacao");
                livro.setData_publicacao(dataSQL != null ? dataSQL.toLocalDate() : null);
                livros.add(livro);
            }
        }
        return livros;
    }

    /**
     * Busca livros por gênero, excluindo o livro de etiqueta fornecida.
     * @param genero
     * @param etiquetaExcluida
     * @return 
     * @throws javax.xml.rpc.ServiceException
     */
    public List<Livro> buscarLivrosPorGenero(String genero, int etiquetaExcluida) throws ServiceException {
        try {
            return livroDAO.buscarLivrosPorGenero(genero, etiquetaExcluida);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livros por gênero", ex);
            throw new ServiceException("Erro ao buscar livros por gênero: " + ex.getMessage(), ex);
        }
    }
    
    /**
     *
     * @param generos
     * @return
     * @throws SQLException
     */
    public List<Livro> buscarLivrosPorGeneros(String[] generos) throws SQLException {
    Connection connection=ConnectionFactory.getConnection();
    StringBuilder sql = new StringBuilder("SELECT * FROM livros WHERE genero IN (");
    for (int i = 0; i < generos.length; i++) {
        sql.append("?");
        if (i < generos.length - 1) {
            sql.append(", ");
        }
    }
    sql.append(")");

    try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
        for (int i = 0; i < generos.length; i++) {
            stmt.setString(i + 1, generos[i].trim());
        }

        List<Livro> livros = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGenero(rs.getString("genero"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setEditora(rs.getString("editora"));
                Date sqlDate = rs.getDate("data_publicacao");
                if (sqlDate != null) {
                    livro.setData_publicacao(sqlDate.toLocalDate());
                }
                livros.add(livro);
            }
        }
        return livros;
    }   
}

        /**
     * Busca livros pelo critério informado no parâmetro.
     * @param filtro Texto a ser utilizado na pesquisa (pode corresponder a qualquer campo).
     * @return Lista de livros que correspondem ao filtro.
     * @throws ServiceException Caso ocorra erro na execução da consulta.
     */
    public List<Livro> buscarLivrosPorFiltro(String filtro) throws ServiceException {
        try {
            // Todos os outros parâmetros como null, o filtro será pesquisado em título, autor, gênero ou ISBN.
            return filtrarLivros(filtro, filtro, filtro, filtro, null);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livros por filtro", ex);
            throw new ServiceException("Erro ao buscar livros por filtro: " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param etiquetaLivro
     * @return
     * @throws ServiceException
     */
public Livro busca_porEtiquetaetiquetaLivro(int etiquetaLivro) throws ServiceException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = ConnectionFactory.getConnection(); // Conexão com o banco de dados

        String sql = "SELECT * FROM livros WHERE etiqueta_livro = ?"; // Ajuste para usar `SMALLINT`
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, etiquetaLivro); // Usa `setInt` para garantir que o tipo seja correto

        rs = stmt.executeQuery();

        if (rs.next()) {
            Livro livro = new Livro();
            livro.setEtiqueta_livro(rs.getInt("etiqueta_livro"));
            livro.setIsbn(rs.getString("isbn"));
            livro.setTitulo(rs.getString("titulo"));
            livro.setAutor(rs.getString("autor"));
            livro.setEditora(rs.getString("editora"));
            livro.setGenero(rs.getString("genero"));

            Date sqlDate = rs.getDate("data_publicacao");
            if (sqlDate != null) {
                livro.setData_publicacao(sqlDate.toLocalDate());
            }

            return livro;
        }

        return null; // Retorna `null` se nenhum livro for encontrado
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Erro ao buscar livro por etiqueta: " + etiquetaLivro, e);
        throw new ServiceException("Erro ao buscar livro por etiqueta: " + e.getMessage(), e);
    } finally {
        // Fecha os recursos
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Erro ao fechar recursos da consulta", e);
        }
    }
}

}
