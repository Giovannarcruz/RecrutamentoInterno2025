package giovanna.projeto.livraria1.util;


import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.LivroService;
import giovanna.projeto.livraria1.services.LivroSimilaresService;
import giovanna.projeto.livraria1.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author giova
 */
public class GerenciadorSimilares {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorSimilares.class.getName());

    /**
     *
     * @throws Exception
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
