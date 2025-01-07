package giovanna.projeto.livraria1.model;

/**
 * A classe <code>Genero</code> representa um gênero literário dentro do sistema de livraria.
 * Cada gênero possui um identificador único (ID) e um nome.
 * 
 * Esta classe é mapeada para a tabela 'genero' no banco de dados, onde o campo 'id'
 * é utilizado como chave primária.
 * 
 * @author Giovanna
 */
public class Genero {

    // ID do gênero (chave primária no banco de dados)
    private int id;

    // Nome do gênero literário
    private String nome;

    /**
     * Construtor padrão, utilizado para criar uma instância de Genero sem parâmetros.
     */
    public Genero() {
    }

    /**
     * Construtor com parâmetros, utilizado para criar uma instância de Genero com um ID e nome específicos.
     * 
     * @param id O identificador único do gênero.
     * @param nome O nome do gênero literário.
     */
    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Retorna o ID do gênero.
     * 
     * @return O ID do gênero.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do gênero.
     * 
     * @param id O novo identificador único do gênero.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do gênero.
     * 
     * @return O nome do gênero literário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do gênero.
     * 
     * @param nome O novo nome do gênero literário.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna uma representação em string do gênero, que é o nome do gênero.
     * 
     * @return O nome do gênero como string.
     */
    @Override
    public String toString() {
        return nome;
    }
}
