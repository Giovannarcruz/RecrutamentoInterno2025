package giovanna.projeto.livraria1.dao;

import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A classe <code>LivroSimilaresDAO</code> fornece os métodos de acesso a dados para manipular
 * as similaridades entre livros na tabela 'livros_semelhantes' do banco de dados.
 * Ela oferece funcionalidades para adicionar, excluir, buscar livros semelhantes e verificar
 * se uma similaridade já existe entre dois livros.
 * 
 * @author Giovanna
 */
public class LivroSimilaresDAO {
    // Logger para registrar erros e informações de execução
    private static final Logger LOGGER = Logger.getLogger(LivroSimilaresDAO.class.getName());

    /**
     * Construtor padrão.
     */
    public LivroSimilaresDAO(){}

    /**
     * Adiciona uma relação de similaridade entre dois livros no banco de dados.
     * 
     * @param etiquetaLivro A etiqueta do livro principal.
     * @param etiquetaSimilar A etiqueta do livro considerado similar.
     * @throws Exception Caso ocorra um erro ao adicionar a similaridade.
     */
    public void adicionarLivroSimilar(int etiquetaLivro, int etiquetaSimilar) throws Exception {
        String sql = "INSERT INTO livros_semelhantes (etiqueta_livro, etiqueta_semelhante) VALUES (?, ?)";
        try (Connection connection= ConnectionFactory.getConnection();PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, etiquetaLivro);
            stmt.setInt(2, etiquetaSimilar);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir similaridade", ex);
            throw ex;
        }
    }
    /**
     * Exclui uma relação de similaridade entre dois livros do banco de dados.
     * 
     * @param etiquetaLivro A etiqueta do livro principal.
     * @param etiquetaSimilar A etiqueta do livro considerado similar.
     * @throws Exception Caso ocorra um erro ao excluir a similaridade.
     */
    public void excluirLivroSimilar(int etiquetaLivro, int etiquetaSimilar) throws Exception {
    String sql = "DELETE FROM livros_semelhantes WHERE etiqueta_livro = ? AND etiqueta_semelhante = ?";
        try (Connection connection= ConnectionFactory.getConnection();PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, etiquetaLivro);
            stmt.setInt(2, etiquetaSimilar);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir similaridade", ex);
            throw ex;
        }
    }
    /**
     * Busca todos os livros semelhantes a um livro, dados sua etiqueta.
     * 
     * @param etiquetaLivro A etiqueta do livro principal.
     * @return Uma lista de objetos <code>Livro</code> que são semelhantes ao livro principal.
     * @throws Exception Caso ocorra um erro ao buscar os livros semelhantes.
     */
    public List<Livro> buscarLivrosSemelhantesEtiquetas(int etiquetaLivro) throws Exception {
        String sql = "SELECT l.* FROM livros l " +
                "INNER JOIN livros_semelhantes ls ON l.etiqueta_livro = ls.etiqueta_semelhante " +
                "WHERE ls.etiqueta_livro = ?";
        List<Livro> livros = new ArrayList<>();
        try (Connection connection= ConnectionFactory.getConnection();PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, etiquetaLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setEtiqueta_livro(rs.getInt("etiqueta_livro"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutor(rs.getString("autor"));
                    // ... set outros atributos do livro
                    livros.add(livro);
                }
            }
        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livros semelhantes", ex);
            throw ex;
        }
        return livros;
    }
     /**
     * Verifica se uma relação de similaridade entre dois livros já existe no banco de dados.
     * 
     * @param etiquetaLivro1 A etiqueta do primeiro livro.
     * @param etiquetaLivro2 A etiqueta do segundo livro.
     * @return <code>true</code> se a similaridade existir, <code>false</code> caso contrário.
     * @throws SQLException Caso ocorra um erro na consulta ao banco de dados.
     */
      public boolean similaridadeExiste(int etiquetaLivro1, int etiquetaLivro2) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livros_semelhantes WHERE etiqueta_livro = ? AND etiqueta_semelhante = ?";
        try (Connection connection= ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, etiquetaLivro1);
            stmt.setInt(2, etiquetaLivro2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true se a similaridade existir
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao executar consulta de similaridade", ex);
            throw ex;
        }
        return false; // Retorna false se ocorrer um erro ou a similaridade não existir
    }
}
