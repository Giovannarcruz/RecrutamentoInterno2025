package giovanna.projeto.livraria1;

import giovanna.projeto.livraria1.util.ConnectionFactory;
import giovanna.projeto.livraria1.view.JanelaPrincipal;
import giovanna.projeto.livraria1.view.LivroDialog;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Giovanna
 */




public class Livraria1Application {
  public static void main(String[] args) {
       Logger LOGGER = Logger.getLogger(Livraria1Application.class.getName());
        // Configuração DO LOGGER (no início do main())
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO); // Exibe TODOS os logs
        
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL); // Certifique-se que o handler também está em ALL
        rootLogger.addHandler(handler);

        // Resto do seu código (conexão com o banco, etc.)
        try (Connection connection = ConnectionFactory.getConnection()) {
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        } catch (SQLException ex) {
            // Logando o erro de conexão CORRETAMENTE
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados", ex);
        }
    }
}
