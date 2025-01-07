package giovanna.projeto.livraria1.util;


import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.LivroService;
import giovanna.projeto.livraria1.services.LivroSimilaresService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Classe responsável por gerenciar o cálculo de similaridades entre livros no banco de dados.
 * A classe realiza a comparação de livros dentro do mesmo gênero e adiciona as relações de similaridade.
 * <p>
 * O cálculo de similaridade envolve a busca de livros do mesmo gênero e a verificação se já existe uma relação de similaridade entre os livros.
 * Se não existir, a similaridade é adicionada para ambos os livros.
 * </p>
 */
public class GerenciadorSimilares {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorSimilares.class.getName());

    /**
     * Calcula as similaridades para todos os livros cadastrados no sistema.
     * Este método percorre todos os livros e, para cada um, busca outros livros do mesmo gênero.
     * Se não houver uma relação de similaridade entre os livros, ela é criada.
     *
     * @throws Exception Se ocorrer um erro durante o processo de cálculo das similaridades.
     */
    public static void calcularSimilaridadesParaLivrosExistentes() throws Exception {
        LivroService livroService = new LivroService(); // Instancia o serviço *fora* do try-with-resources da transação
        try (Connection connection = ConnectionFactory.getConnection()) { // Conexão para a transação principal
            connection.setAutoCommit(false);

            try {
                List<Livro> todosOsLivros = livroService.lista_livros();

                if (todosOsLivros != null) {
                    for (Livro livro : todosOsLivros) {
                        List<Livro> livrosDoMesmoGenero = livroService.buscarLivrosPorGenero(livro.getGenero(), livro.getEtiqueta_livro());

                        if (livrosDoMesmoGenero != null) {
                            for (Livro livroSimilar : livrosDoMesmoGenero) {
                                try (Connection innerConnection = ConnectionFactory.getConnection()) { // Conexão *dentro* do loop
                                    LivroSimilaresService similaresService = new LivroSimilaresService(); // Instancia o serviço *dentro* do loop
                                    if (!similaresService.similaridadeExiste(livro.getEtiqueta_livro(), livroSimilar.getEtiqueta_livro())) {
                                        similaresService.adicionarLivroSimilar(livro.getEtiqueta_livro(), livroSimilar.getEtiqueta_livro());
                                        similaresService.adicionarLivroSimilar(livroSimilar.getEtiqueta_livro(), livro.getEtiqueta_livro());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Não há livros cadastrados para calcular similaridades.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }

                connection.commit();
                System.out.println("Similaridades recalculadas com sucesso!");

            } catch (SQLException ex) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "Erro durante o recálculo de similaridades (rollback efetuado)", ex);
                JOptionPane.showMessageDialog(null, "Erro ao calcular similaridades: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                throw new Exception("Erro ao calcular similaridades (transação revertida): " + ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao obter conexão para a transação principal", ex);
            JOptionPane.showMessageDialog(null, "Erro ao obter a conexão com o banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Erro ao obter a conexão com o banco de dados: " + ex.getMessage());
        } finally {
            try {
                
            } catch (Throwable ex) {
                LOGGER.log(Level.WARNING, "Erro ao finalizar LivroService", ex);
            }
        }
    }
}
