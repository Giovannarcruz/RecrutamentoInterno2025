package giovanna.projeto.livraria1.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Classe para configurar o logging da aplicação, registrando logs em um arquivo.
 */
public class LogtoFile {
    private static final Logger LOGGER = Logger.getLogger(LogtoFile.class.getName());

    static {
        try {
            // Configura o FileHandler para salvar logs em "logs.txt", com o modo append ativado
            FileHandler fileHandler = new FileHandler("logs.txt", true); // 'true' para adicionar ao arquivo existente
            fileHandler.setLevel(Level.ALL); // Define o nível mínimo de logs que será registrado
            fileHandler.setFormatter(new SimpleFormatter()); // Formatação simples de logs

            // Adiciona o FileHandler ao logger
            LOGGER.addHandler(fileHandler);

            // Remove o console handler padrão, para não duplicar logs
            LOGGER.setUseParentHandlers(false);

        } catch (IOException e) {
            // Se ocorrer um erro ao configurar o FileHandler, loga esse erro no console
            LOGGER.log(Level.SEVERE, "Erro ao configurar o FileHandler para logs", e);
        }
    }

    /**
     * Exemplo de como registrar um log de informação.
     * @param message
     */
    public static void logInfo(String message) {
        LOGGER.log(Level.INFO, message);
    }

    /**
     * Exemplo de como registrar um log de erro.
     * @param message
     * @param throwable
     */
    public static void logError(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }
}
