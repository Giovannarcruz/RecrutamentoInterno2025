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
 * Classe responsável pelas manipulações de dados na tabela de gêneros no banco
 * de dados.
 *
 * Esta classe fornece métodos para listar, salvar, atualizar e excluir gêneros
 * na base de dados. Ela utiliza consultas SQL predefinidas e se conecta ao
 * banco de dados por meio da classe {@link ConnectionFactory}.
 *
 * @author Giovanna
 */
public class GeneroDAO {

    private final String SQL_LISTA_GENEROS = "SELECT * FROM generos ORDER BY nome";
    private final String SQL_SALVA_GENERO = "INSERT INTO generos (nome) VALUES (?)";
    private static final String SQL_ATUALIZA_GENERO = "UPDATE generos SET nome= ? WHERE id= ?";
    private static final String SQL_EXCLUI_GENERO = "DELETE FROM generos WHERE id=?";

    /**
     * Lista todos os gêneros cadastrados na base de dados.
     *
     * Este método executa uma consulta SQL para listar todos os gêneros
     * presentes na tabela `generos`, ordenados pelo nome. Os resultados são
     * mapeados para objetos {@link Genero} e retornados em uma lista.
     *
     * @return Uma lista de objetos {@link Genero} contendo os gêneros
     * cadastrados na base de dados.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
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
     * Salva um novo gênero na base de dados.
     *
     * Este método executa uma consulta SQL do tipo {@code INSERT} para salvar
     * um novo gênero no banco de dados. O gênero a ser salvo é passado como
     * parâmetro e seus dados são inseridos na tabela generos.
     *
     * @param genero O objeto {@link Genero} contendo os dados do gênero a ser
     * salvo.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    // Salva o novo gênero na base de dados. Primeiro tenta realizar a conexão e então executa o SQL informado.
    public void salvaGenero(Genero genero) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareCall(SQL_SALVA_GENERO)) {
            stmt.setString(1, genero.getNome());
            stmt.executeUpdate();

        }
    }

    /**
     * Atualiza um gênero pré-existente na base de dados.
     *
     * Este método executa uma consulta SQL do tipo {@code UPDATE} para
     * modificar um gênero existente na tabela `generos`. O gênero a ser
     * atualizado é identificado pelo seu {@code id}.
     *
     * @param genero O objeto {@link Genero} contendo os dados do gênero a ser
     * atualizado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
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
     * Exclui um gênero da base de dados.
     *
     * Este método executa uma consulta SQL do tipo {@code DELETE} para remover
     * um gênero da tabela `generos`, identificado pelo seu {@code id}.
     *
     * @param id O {@code id} do gênero a ser excluído.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public void excluiGenero(int id) throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement stmt = connection.prepareStatement(SQL_EXCLUI_GENERO)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
