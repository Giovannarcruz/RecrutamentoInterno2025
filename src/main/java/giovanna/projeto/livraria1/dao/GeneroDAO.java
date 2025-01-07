package giovanna.projeto.livraria1.dao;

import giovanna.projeto.livraria1.model.Genero;
import giovanna.projeto.livraria1.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Método que realiza as manipulações de dados na tabela generos.
 * @author Giovanna
 */
public class GeneroDAO {

    private final String SQL_LISTA_GENEROS = "SELECT * FROM generos ORDER BY nome";
    private final String SQL_SALVA_GENERO = "INSERT INTO generos (nome) VALUES (?)";
    private static final String SQL_ATUALIZA_GENERO = "UPDATE generos SET nome= ? WHERE id= ?";
    private static final String SQL_EXCLUI_GENERO = "DELETE FROM generos WHERE id=?";

    //Lista todos os gêneros
    /**
     * Método que lista todos os gêneros cadastrados na base de dados
     *
     * @return Gêneros cadastrados na base de dasos.
     * @throws SQLException
     */
    // Tenta realizar a conexão com o banco, realizar o select (que consta na variável SQL_LISTA_GENEROS e então executar a cosnulta.
    public List<Genero> listaGeneros() throws SQLException {
        List<Genero> generos = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareCall(SQL_LISTA_GENEROS); ResultSet result = stmt.executeQuery()) {
            while (result.next()) {
                Genero genero = new Genero();
                genero.setId(result.getInt("id"));
                genero.setNome(result.getString("nome"));
                generos.add(genero);
            }

        }
        return generos;
    }

    /**
     * Método utilizado para salvar um novo gênero dentro do banco de dados por
     * meio de um Insert.
     *
     * @param genero é o gênero que será inserido.
     * @throws SQLException
     */
    // Salva o novo gênero na base de dados. Primeiro tenta realizar a conexão e então executa o SQL informado.
    public void salvaGenero(Genero genero) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareCall(SQL_SALVA_GENERO)) {
            stmt.setString(1, genero.getNome());
            stmt.executeUpdate();

        }
    }

    /**
     * Método utilizado para atualizar um gênero pré-existente, selecionado pelo
     * usuário na interface gráfica
     *
     * @param genero é o gênero do livro cadastrado
     * @throws SQLException
     */
    public void atualizaGenero(Genero genero) throws SQLException {
        //Tenta realizar a conexão e então executa a atualização por meio do SQL informado.
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SQL_ATUALIZA_GENERO)) {
            stmt.setString(1, genero.getNome());
            stmt.setInt(2, genero.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Método que exclui um gênero de acordo com o id dele.
     *
     * @param id é a identificação do livro
     * @throws SQLException
     */
    public void excluiGenero(int id) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SQL_EXCLUI_GENERO)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
