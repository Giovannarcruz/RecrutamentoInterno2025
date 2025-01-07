package giovanna.projeto.livraria1.dao;

import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Método responsável pelas manipulações no banco de dados, mais especificamente
 * na tabela livros.
 *
 * @author Giovanna
 */
public class LivroDAO {

    // Declaração de variáveis.
    // Logger utilizado para categorizar e exibir os logs gerados
    private static final Logger LOGGER = Logger.getLogger(LivroDAO.class.getName()); // Logger para a classe DAO
    // Querys utilizadas para a manipulação no banco
    private static final String INSERT_LIVRO_SQL
            = "INSERT INTO livros(titulo, autor, editora, genero, isbn, data_publicacao, data_inclusao) VALUES (?,?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_LIVRO_SQL
            = "UPDATE livros SET titulo = ?, autor = ?, editora = ?, genero = ?, isbn = ?, data_publicacao = ? WHERE etiqueta_livro = ?";
    private static final String SELECT_LIVRO_SQL = "SELECT * FROM livros";
    private static final String SELECT_LIVRO_ETIQUETA_SQL = "SELECT * FROM livros WHERE etiqueta_livro=?";
    private static final String DELETE_LIVRO_SQL = "DELETE FROM livros WHERE etiqueta_livro = ?";
    private static final String LISTA_LIVROS_SQL = "SELECT * FROM livros";
    private static final String SELECT_LIVRO_POR_ISBN_SQL = "SELECT * FROM livros WHERE isbn=?";
    private static final String INSERT_LIVRO_SQL_SEM_ETIQUETA = "INSERT INTO livros(titulo, autor, editora, genero, isbn, data_publicacao, data_inclusao) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * Construtor vazio da classe
     */
    public LivroDAO() {
    }

    /**
     * Método utilizado para inserir um livro na base dados.
     *
     * @param livro objeto livro que contém os dados do livro.
     * @throws SQLException caso ocorra algum problema ao realizar a operação.
     */
    public void inserirLivro(Livro livro) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(INSERT_LIVRO_SQL_SEM_ETIQUETA, Statement.RETURN_GENERATED_KEYS)) {

            preencherStatementComLivroSemEtiqueta(stmt, livro);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setEtiqueta_livro(generatedKeys.getInt(1)); // Obtém a etiqueta gerada
                } else {
                    throw new SQLException("Falha ao obter a etiqueta gerada.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir o livro: " + livro.getTitulo(), e);
            throw e;
        }
    }

    /**
     * Método para efetuar a edição de um livro pré-existente.
     *
     * @param livro livro que será alterado
     * @throws SQLException caso ocorra algum problema ao realizar a operação.
     */
    public void alterarLivro(Livro livro) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(UPDATE_LIVRO_SQL)) {
            preencherStatementComLivroParaUpdate(stmt, livro);
            // pega a etiqueta do livro e então o coloca na query.
            stmt.setInt(7, livro.getEtiqueta_livro());
            // variável que contém quantitade de colunas atualizadas, se nenhuma for atualizada retorna um log correspondente
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.log(Level.WARNING, "Nenhuma etiqueta encontrada para atualização: " + livro.getEtiqueta_livro());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar o livro: " + livro.getTitulo(), e);
            throw e;
        }
    }

    /**
     * Método para preencher a query quando for uma inclusão
     *
     * @param stmt Statement
     * @param livro Livro que será utilizado para preencher a consulta.
     */
    private void preencherStatementComLivroSemEtiqueta(PreparedStatement stmt, Livro livro) throws SQLException {
        stmt.setString(1, livro.getTitulo()); // Posição 1 (Título)
        stmt.setString(2, livro.getAutor());   // Posição 2
        stmt.setString(3, livro.getEditora()); // Posição 3
        stmt.setString(4, livro.getGenero()); // Posição 4
        stmt.setString(5, livro.getIsbn());   // Posição 5

        if (livro.getData_publicacao() != null) {
            stmt.setDate(6, Date.valueOf(livro.getData_publicacao())); // Posição 6
        } else {
            stmt.setDate(6, null);
        }

        stmt.setDate(7, Date.valueOf(LocalDate.now())); // Posição 7 (Data de inclusão)
    }

    /**
     * Método que preenche o Statement quando se trata de um Update na base.
     *
     * @param stmt Statement
     * @param livro Livro que será utilizado para preenchimento da query.
     * @throws SQLException caso ocorra algum problema ao realizar a operação.
     */
    private void preencherStatementComLivroParaUpdate(PreparedStatement stmt, Livro livro) throws SQLException {
        stmt.setString(1, livro.getTitulo());
        stmt.setString(2, livro.getAutor());
        stmt.setString(3, livro.getEditora());
        stmt.setString(4, livro.getGenero());
        stmt.setString(5, livro.getIsbn());

        if (livro.getData_publicacao() != null) {
            stmt.setDate(6, Date.valueOf(livro.getData_publicacao()));
        } else {
            stmt.setDate(6, null);
        }
    }

    /**
     * Método que efetua a exclusão de um livro, tendo como informação sua
     * etiqueta.
     *
     * @param etiqueta_livro é a etiqueta do livro que será excluído
     * @throws SQLException caso ocorra algum problema ao realizar a operação.
     */
    public void excluirLivro(int etiqueta_livro) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(DELETE_LIVRO_SQL)) {
            // pega o valor da etiqueta, e o coloca no statement
            stmt.setInt(1, etiqueta_livro);
            // Vê o número de linhas afetadas e se nenhuma for afetada retorna um log.
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.log(Level.WARNING, "Nenhum livro encontrado para exclusão com etiqueta: " + etiqueta_livro);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro na exclusão do livro com etiqueta: " + etiqueta_livro, e);
            throw e;
        }
    }

    /**
     * Método para consultar os livros cadastrados na base
     *
     * @return livros retornados pela consulta
     * @throws java.sql.SQLException caso ocorra algum problema ao realizar a
     * operação.
     */
    public List<Livro> consultaLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_LIVRO_SQL); ResultSet rs = stmt.executeQuery()) {
            // enquanto houver próximo, será acrescentado um livro na lista que será exibida
            while (rs.next()) {
                Livro livro = criarLivroDoResultSet(rs);
                livros.add(livro);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar os livros!", e);
            throw e;
        }
        return livros; // retorna os livros encontrados
    }

    /**
     * Método para consultar um livro específico por meio de sua etiqueta
     *
     * @param etiqueta_livro etiqueta do livro que deverá ser consultada
     * @return livro que possui a etiqueta especificada.
     * @throws SQLException caso ocorra algum problema ao realizar a operação.
     */
    public Livro busca_porEtiqueta(String etiqueta_livro) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_LIVRO_ETIQUETA_SQL)) {
            stmt.setString(1, etiqueta_livro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarLivroDoResultSet(rs); // converte e retorna os livros de acordo com os padrões do sistema
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livro por etiqueta: " + etiqueta_livro, e);
            throw e;
        }
    }

    /**
     * Método que realiza a busca de um livro por meio de seu ISBN
     *
     * @param isbn isbn que será consultado para encontrar o livro
     * correspondente
     * @return livro com o ISBN especificado
     * @throws SQLException caso ocorra algum problema durante a execução da
     * query
     */
    public Livro busca_porISBN(String isbn) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_LIVRO_POR_ISBN_SQL)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarLivroDoResultSet(rs); // converte e retorna os livros de acordo com os padrões do sistema
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livro por ISBN: " + isbn, e);
            throw e;
        }

    }

    /**
     * Método para listar os livros cadastrados na base de dados
     *
     * @return livros cadastrados na base de dados.
     * @throws SQLException caso ocorra algum problema durante a execução da
     * query
     */
    public List<Livro> listaLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(LISTA_LIVROS_SQL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = criarLivroDoResultSet(rs); // instancia um livro conforme o método utilizado para converter as informações para o padrão do sistema
                livros.add(livro); // adiciona o livro na lista.
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todos os livros", e);
            throw e;
        }
        return livros; // exibe a lista de livros
    }

    /**
     * Cria um objeto {@link Livro} a partir de um {@link ResultSet}.
     *
     * Este método extrai os dados de um {@link ResultSet} e os usa para
     * preencher os atributos de um objeto {@link Livro}. Ele mapeia as colunas
     * do {@link ResultSet} para os campos da classe {@link Livro}, incluindo a
     * conversão da data de publicação para o tipo {@link LocalDate}, se
     * necessário.
     *
     * @param rs O {@link ResultSet} contendo os dados do livro a serem
     * extraídos.
     * @return O objeto {@link Livro} com os dados extraídos do
     * {@link ResultSet}.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do
     * {@link ResultSet}.
     */
    private Livro criarLivroDoResultSet(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setEtiqueta_livro(rs.getInt("etiqueta_livro"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setEditora(rs.getString("editora"));
        livro.setGenero(rs.getString("genero"));
        livro.setIsbn(rs.getString("isbn"));

        Date dataSQL = rs.getDate("data_publicacao");
        if (dataSQL != null) {
            livro.setData_publicacao(dataSQL.toLocalDate()); // Conversão crucial!
        }

        livro.setData_inclusao(rs.getDate("data_inclusao"));
        return livro;
    }

    /**
     * Verifica se um livro com o ISBN especificado existe na base de dados.
     *
     * Este método executa uma consulta SQL para verificar a existência de um
     * livro com o ISBN fornecido. Ele retorna {@code true} se o livro existir,
     * caso contrário retorna {@code false}.
     *
     * @param isbn O ISBN do livro a ser verificado.
     * @return {@code true} se o livro com o ISBN fornecido existir,
     * {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao acessar a base de dados.
     */
    public boolean VerificaLivro(String isbn) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement("SELECT 1 FROM livros WHERE isbn = ?")) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                LOGGER.info("Resultado se livro existe: " + rs.next());
                return rs.next(); // Retorna true se encontrar um registro com o ISBN, false caso contrário
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar livro por ISBN: " + isbn, e);
            throw e; //relança a exceção
        }
    }

    /**
     * Busca livros de um determinado gênero, excluindo o livro com a etiqueta
     * fornecida.
     *
     * Este método executa uma consulta SQL para buscar todos os livros que
     * pertencem ao gênero especificado, excluindo o livro com a etiqueta
     * fornecida. A lista resultante de livros é retornada.
     *
     * @param genero O gênero dos livros a serem buscados.
     * @param etiquetaExcluida A etiqueta do livro que deve ser excluído da
     * busca.
     * @return Uma lista de livros que pertencem ao gênero especificado,
     * excluindo o livro com a etiqueta fornecida.
     * @throws SQLException Se ocorrer um erro ao acessar a base de dados.
     */
    public List<Livro> buscarLivrosPorGenero(String genero, int etiquetaExcluida) throws SQLException {
        List<Livro> livrosDoMesmoGenero = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE genero = ? AND etiqueta_livro <> ?"; // Exclui o livro atual

        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, genero);
            stmt.setInt(2, etiquetaExcluida);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = criarLivroDoResultSet(rs);
                    livrosDoMesmoGenero.add(livro);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livros por gênero", e);
            throw e;
        }
        return livrosDoMesmoGenero; // retorna os livros de mesmo gênero da etiqueta
    }

    /**
     * Filtra livros com base nos critérios fornecidos.
     *
     * Este método constrói dinamicamente uma consulta SQL para buscar livros
     * que correspondam aos critérios fornecidos. Ele permite filtrar os livros
     * por vários atributos, como etiqueta, título, autor, gênero, ISBN e data
     * de publicação. A consulta é executada e uma lista de livros que atendem
     * aos critérios de filtragem é retornada.
     *
     * @param connection A conexão com o banco de dados.
     * @param etiqueta A etiqueta do livro a ser filtrado (opcional).
     * @param titulo O título do livro a ser filtrado (opcional).
     * @param autor O autor do livro a ser filtrado (opcional).
     * @param genero O gênero do livro a ser filtrado (opcional).
     * @param isbn O ISBN do livro a ser filtrado (opcional).
     * @param data_publicacao A data de publicação do livro a ser filtrado
     * (opcional).
     * @return Uma lista de livros que atendem aos critérios de filtragem.
     * @throws SQLException Se ocorrer um erro ao executar a consulta no banco
     * de dados.
     */
    public List<Livro> filtrarLivros(Connection connection, String etiqueta, String titulo, String autor, String genero, String isbn, LocalDate data_publicacao) throws SQLException {
        // Monta a query SQL dinamicamente com base nos filtros fornecidos
        StringBuilder sql = new StringBuilder("SELECT * FROM livros WHERE 1=1");

        // Lista de filtros para serem aplicados
        List<Object> filtros = new ArrayList<>();

        if (etiqueta != null && !etiqueta.isBlank()) {
            sql.append(" AND CAST(etiqueta_livro AS VARCHAR) LIKE ?");
            filtros.add("%" + etiqueta + "%");
        }
        if (titulo != null && !titulo.isBlank()) {
            sql.append(" AND titulo LIKE ?");
            filtros.add("%" + titulo + "%");
        }
        if (autor != null && !autor.isBlank()) {
            sql.append(" AND autor LIKE ?");
            filtros.add("%" + autor + "%");
        }
        if (genero != null && !genero.isBlank()) {
            sql.append(" AND genero LIKE ?");
            filtros.add("%" + genero + "%");
        }
        if (isbn != null && !isbn.isBlank()) {
            sql.append(" AND isbn LIKE ?");
            filtros.add("%" + isbn + "%");
        }
        if (data_publicacao != null) {
            sql.append(" AND data_publicacao = ?");
            filtros.add(Date.valueOf(data_publicacao));
        }

        // Prepara a consulta
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            // Define os valores dos parâmetros
            for (int i = 0; i < filtros.size(); i++) {
                stmt.setObject(i + 1, filtros.get(i));
            }
            LOGGER.info("Query: " + sql + ", Parâmetros: " + filtros);
            // Executa a consulta e retorna os resultados
            return executarConsulta(stmt);

        }
    }

    /**
     * Executa a consulta SQL fornecida e retorna os livros encontrados.
     *
     * Este método executa a consulta SQL passada como parâmetro e mapeia os
     * resultados retornados para uma lista de objetos {@link Livro}. Ele
     * utiliza um {@link ResultSet} para extrair os dados dos livros e preenche
     * a lista com esses livros.
     *
     * @param stmt O {@link PreparedStatement} contendo a consulta SQL a ser
     * executada.
     * @return Uma lista de livros resultantes da consulta SQL.
     * @throws SQLException Se ocorrer um erro ao executar a consulta ou
     * processar os resultados.
     */
    private List<Livro> executarConsulta(PreparedStatement stmt) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        LOGGER.log(Level.INFO, "Executando SQL: {0}", stmt);
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

}
