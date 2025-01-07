/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package giovanna.projeto.livraria1.src.util;

import giovanna.projeto.livraria1.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author giova
 */
public class ConnectionFactoryTest {
    
      @Test
    void testGetConnection() {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Assertions.assertNotNull(connection, "A conexão não deve ser nula.");
            Assertions.assertFalse(connection.isClosed(), "A conexão deve estar aberta.");
            connection.close();
        } catch (SQLException e) {
            Assertions.fail("Erro inesperado ao obter conexão: " + e.getMessage());
        }
    }

}
