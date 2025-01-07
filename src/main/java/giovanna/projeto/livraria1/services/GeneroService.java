package giovanna.projeto.livraria1.services;

import giovanna.projeto.livraria1.dao.GeneroDAO;
import giovanna.projeto.livraria1.model.Genero;
import java.sql.SQLException;
import java.util.List;

/**
 * A classe <code>GeneroService</code> gerencia as operações relacionadas ao gênero literário.
 * Ela atua como intermediária entre o controlador e o <code>GeneroDAO</code>, facilitando a
 * manipulação dos dados relacionados aos gêneros no banco de dados.
 * 
 * As operações incluem listar, salvar, atualizar e excluir gêneros.
 * 
 * @author Giovanna
 */
public class GeneroService {

    // Objeto para acessar os métodos de manipulação de dados da classe GeneroDAO
    private final GeneroDAO generoDAO;

    /**
     * Construtor da classe <code>GeneroService</code>.
     * Inicializa o objeto <code>GeneroDAO</code> para acesso aos métodos de manipulação de dados.
     */
    public GeneroService() {
        this.generoDAO = new GeneroDAO();
    }

    /**
     * Lista todos os gêneros cadastrados no banco de dados.
     * 
     * @return Uma lista de objetos <code>Genero</code> com todos os gêneros registrados.
     * @throws SQLException Caso ocorra um erro na consulta ao banco de dados.
     */
    public List<Genero> listaGeneros() throws SQLException {
        return generoDAO.listaGeneros();
    }

    /**
     * Salva um novo gênero no banco de dados.
     * 
     * @param genero O objeto <code>Genero</code> a ser salvo.
     * @throws SQLException Caso ocorra um erro ao salvar o gênero no banco de dados.
     */
    public void salvaGenero(Genero genero) throws SQLException {
        generoDAO.salvaGenero(genero);
    }

    /**
     * Atualiza um gênero previamente existente no banco de dados.
     * 
     * @param genero O objeto <code>Genero</code> com as informações atualizadas.
     * @throws SQLException Caso ocorra um erro ao atualizar o gênero no banco de dados.
     */
    public void atualizaGenero(Genero genero) throws SQLException {
        generoDAO.atualizaGenero(genero);
    }

    /**
     * Exclui um gênero do banco de dados com base no seu identificador (ID).
     * 
     * @param id O ID do gênero a ser excluído.
     * @throws SQLException Caso ocorra um erro ao excluir o gênero no banco de dados.
     */
    public void excluiGenero(int id) throws SQLException {
        generoDAO.excluiGenero(id);
    }
}
