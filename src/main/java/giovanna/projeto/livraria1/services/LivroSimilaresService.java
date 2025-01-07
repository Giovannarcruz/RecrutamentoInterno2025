package giovanna.projeto.livraria1.services;

import giovanna.projeto.livraria1.dao.LivroSimilaresDAO;
import giovanna.projeto.livraria1.model.Livro;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe reponsável por intermediar as operações realizadas na classe similaresDAO, responsável por manipulações no banco de dados no que se diz respeito à tabela de livros semelhantes.
 * @author Giovanna
 */
public class LivroSimilaresService {

    private static final Logger LOGGER = Logger.getLogger(LivroSimilaresService.class.getName());
    private LivroSimilaresDAO similaresDAO;

    /**
     * Construtor da classe
     */
    public LivroSimilaresService() {
        this.similaresDAO = new LivroSimilaresDAO();
    }

    /**
     * Método que verifica se há similaridade entre dois livros.
     * @param etiquetaLivro1 etiqueta do livro selecionado
     * @param etiquetaLivro2 etiqueta do livro que poderá se relacionar com o livro selecionado.
     * @return a verificação se os dois livros são ou não similares.
     * @throws Exception
     */
    public boolean similaridadeExiste(int etiquetaLivro1, int etiquetaLivro2) throws Exception {
        try {
            return similaresDAO.similaridadeExiste(etiquetaLivro1, etiquetaLivro2);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar similaridade", ex);
            throw new Exception("Erro ao verificar similaridade: " + ex.getMessage()); // Relança com mensagem mais amigável
        }
    }

    /**
     * Método que adiciona a similiaridade entre dois livros
     * @param etiquetaLivro livro selecionado
     * @param etiquetaSimilar livro que irá ser considerado similar
     * @throws Exception
     */
    public void adicionarLivroSimilar(int etiquetaLivro, int etiquetaSimilar) throws Exception {
        try {
            similaresDAO.adicionarLivroSimilar(etiquetaLivro, etiquetaSimilar);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao adicionar similaridade", ex);
            throw new Exception("Erro ao adicionar similaridade: " + ex.getMessage());
        }
    }

    /**
     *
     * @param etiquetaLivro
     * @param etiquetaSimilar
     * @throws Exception
     */
    public void excluirLivroSimilar(int etiquetaLivro, int etiquetaSimilar) throws Exception {
        try {
            similaresDAO.excluirLivroSimilar(etiquetaLivro, etiquetaSimilar);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir similaridade", ex);
            throw new Exception("Erro ao excluir similaridade: " + ex.getMessage());
        }
    }

    /**
     * Método que lista os livros que são semelhantes
     * @param etiquetaLivro é a etiqueta do livro que se deseja ver os semelhantes
     * @return os livros semelhantes
     * @throws SQLException
     * @throws Exception
     */
    public List<Livro> buscarLivrosSemelhantes(int etiquetaLivro) throws SQLException, Exception {
        try {
            return similaresDAO.buscarLivrosSemelhantesEtiquetas(etiquetaLivro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livros semelhantes", ex);
            throw new Exception("Erro ao buscar livros semelhantes: " + ex.getMessage());
        }
    }
}