package giovanna.projeto.livraria1.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável pela conexão com o banco, utilizando como parâmetro as credenciais fornecidas no arquivo DBConfig.properties.
 * Fornecendo métodos para obter e fechar as conexões.
 * Dados incluídos:
 * URL do banco de dados
 * Usuário
 * Senha
 * Utiliza o PostgreSQL
 * @author Giovanna
 */
public class ConnectionFactory {
    //LOGGER que é utilizado para registro de eventos e erros
    private static final Logger LOGGER= Logger.getLogger(ConnectionFactory.class.getName());
    // Credenciais informadas no "DBConfig.properties".
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    
    // Bloco estático para carregar as informações e inicializar.
    static{
    try (InputStream input= ConnectionFactory.class.getClassLoader().getResourceAsStream("DBConfig.properties")){
         if(input==null){
             LOGGER.log(Level.SEVERE, "Arquivo properties não localizado!");
       }
        //cria um parâmetro properties e então o carrega com os dados do "DBConfig.properties". 
        Properties properties= new Properties();
        properties.load(input);
        DB_URL= properties.getProperty("db_url");
        DB_USER= properties.getProperty("db_user");
        DB_PASSWORD= properties.getProperty("db_password");
        
        // Tenta carregar o Driver do Postgres, se não conseguir será exibida uma mensagem apropriada. tenta realizar a conexão com base no properties também.
        Class.forName("org.postgresql.Driver");
        }   catch (IOException ex) {
            System.out.println("Não foi carregar o arquivo properties");
            LOGGER.log(Level.SEVERE, "Ocorreu um erro ao carregar as informações do arquivo DBConfig.properties",ex);
            throw new RuntimeException("Ocorreu um erro ao carregar as informações do arquivo DBConfig.properties",ex);
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Não foi possível carregar o driver do Postgres!");
            LOGGER.log(Level.SEVERE, "Driver do PostgreSQL não foi localizado!",ex );
            throw new RuntimeException("Driver do PostgreSQL não foi localizado!",ex);
        }
    }
    
    /**
     * Método que estabelece a conexão com o banco de dados utilizando as credenciais informadas no "DBConfig.properties".
     * @return Um objeto connection que representa a conexão com o banco de dados.
     * @throws SQLException se houver algum problema na conexão.
     */
    private static Connection connection; 

    /**
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
    /**
     *Método para fechar a conexão com o banco de dados.
     */
    public static void closeSharedConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão compartilhada fechada!");
                LOGGER.info("Conexão compartilhada fechada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão compartilhada.");
                LOGGER.log(Level.WARNING, "Erro ao fechar conexão compartilhada", e);
            }
        }
    }

    
}
