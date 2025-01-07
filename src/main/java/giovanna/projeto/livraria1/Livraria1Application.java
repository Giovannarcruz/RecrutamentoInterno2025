package giovanna.projeto.livraria1;

import giovanna.projeto.livraria1.util.ConnectionFactory;
import giovanna.projeto.livraria1.view.JanelaPrincipal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Classe principal da aplicação Livraria1.
 * <p>
 * Esta classe inicializa a aplicação, configurando o logger global e estabelecendo a conexão
 * com o banco de dados. Em seguida, ela exibe a janela principal da interface gráfica.
 * <p>
 * O logger é configurado para exibir informações detalhadas, incluindo mensagens de erro e
 * eventos importantes. A conexão com o banco de dados é criada e gerenciada utilizando
 * a classe {@link ConnectionFactory}.
 * </p>
 * 
 * <h2>Fluxo de execução:</h2>
 * <ul>
 * <li>Configura o logger global para exibir mensagens detalhadas.</li>
 * <li>Tenta estabelecer a conexão com o banco de dados.</li>
 * <li>Exibe a janela principal da aplicação ({@link JanelaPrincipal}).</li>
 * <li>Em caso de falha de conexão, registra o erro no logger.</li>
 * </ul>
 * 
 * @author Giovanna
 */
public class Livraria1Application {

    /**
     * Método principal da aplicação.
     * <p>
     * Configura o logger global, cria a conexão com o banco de dados e inicia a interface
     * gráfica da aplicação. Todos os erros durante a conexão com o banco são registrados
     * no logger.
     * </p>
     * 
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Logger LOGGER = Logger.getLogger(Livraria1Application.class.getName());

        // Configuração do Logger global
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO); // Exibe todos os logs de nível INFO ou superior

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL); // Configura o handler para capturar todos os níveis de log
        rootLogger.addHandler(handler);

        // Inicializa a aplicação
        try (Connection connection = ConnectionFactory.getConnection()) {
            // Exibe a janela principal da aplicação
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        } catch (SQLException ex) {
            // Registra no logger caso a conexão com o banco de dados falhe
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados", ex);
        }
    }
}
