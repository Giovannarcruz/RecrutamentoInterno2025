package giovanna.projeto.livraria1.model;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Giovanna Classe que instancia o objeto Livro. Contém suas
 * informações.
 */
public class Livro {

    //Criação de variáveis
    private int etiqueta_livro;  // etiqueta do livro em questão, é única e será usada como identificadora.
    private String titulo; //título do livro.
    private String autor; //autor(es) do livro.
    private String editora; //editora que públicou o livro.
    private String genero; // gênero literário.
    private String isbn;  //ISBN do livro, é uma espécie de código de barras.
    private LocalDate data_publicacao; // data em que o livro foi publicado.
    private Date data_inclusao;  //data em que o livro foi incluído em sistema.
    private Date data_alteracao; //data em que o livro foi alterado pela última vez em sistema.
    private String data_publicacao_str; //data de publicação como string

    //Construtores
    /**
     * Construtor do objeto livro.
     *
     * @param etiqueta_livro é única, sendo utilizada como identificadora
     * @param titulo é o nome do livro.
     * @param autor quem escreveu o livro.
     * @param editora empresa responsável pela publicação do livro.
     * @param genero classificação teórica do livro de acordo com suas
     * características específicas.
     * @param isbn (International Standard Book Number), é uma sequência
     * numérica que funciona como identificadora do livro.
     * @param data_publicacao Data em que o livro foi publicado pela editora.
     */
    public Livro(int etiqueta_livro, String titulo, String autor, String editora, String genero, String isbn, LocalDate data_publicacao) {
        this.etiqueta_livro = etiqueta_livro;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.genero = genero;
        this.isbn = isbn;
        this.data_publicacao = data_publicacao;
    }

    /**
     * Construtor vazio do objeto livro, pode ser utilizado para inicializar o
     * objeto no código.
     */
    public Livro() {
    }

    //Getters e Setters
    /**
     * Método get para o atributo etiqueta_livro.
     *
     * @return etiqueta do livro (etiqueta_livro).
     */
    public int getEtiqueta_livro() {
        return etiqueta_livro;
    }

    /**
     *
     * @param etiqueta_livro etiqueta do livro
     */
    public void setEtiqueta_livro(int etiqueta_livro) {
        this.etiqueta_livro = etiqueta_livro;
    }

    /**
     * Método get do atributo título
     *
     * @return o título do livro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método set, que define o título do livro
     *
     * @param titulo- é o título do livro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Método get do parâmetro Autor
     *
     * @return o autor do livro.
     */
    public String getAutor() {
        return autor;
    }
    /**
     * Método get do parâmetro data_publicacao_str
     * @return a data de publicação do livro em formato de String
     */
    public String getData_publicacao_str() {
        return data_publicacao_str;
    }
    /**
     * Método que define o valor do parâmetro data_publicacao_str do livro
     * @param data_publicacao_str data de publicação do livro em formato de String
     */

    public void setData_publicacao_str(String data_publicacao_str) {
        this.data_publicacao_str = data_publicacao_str;
    }

    /**
     * Método que define o valor do parâmetro autor do objeto livro.
     *
     * @param autor é o autor do livro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Método get do parâmetro Editora do livro
     *
     * @return o parâmetro editora do objeto Livro.
     */
    public String getEditora() {
        return editora;
    }

    /**
     * Método que define o valor do parâmetro editora do objeto livro.
     *
     * @param editora é a editora do livro.
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Método get do parâmetro Genero
     *
     * @return Genero literário do livro.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Método set que define o valor do parâmetro genero do objeto Livro.
     *
     * @param genero- gênero literário do livro.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Método para obter o parâmetro ISBN do objeto livro
     *
     * @return ISBN do livro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Método que define o valor do parâmetro ISBN do objeto livro.
     *
     * @param isbn - sequência numérica identificadora do livro (International
     * Standard Book Number).
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Método para obter o parâmetro data_publicacao do objeto livro
     *
     * @return a data de publicação do livro
     */
    public LocalDate getData_publicacao() {
        return data_publicacao;
    }

    /**
     * Método que define o valor do parâmetro data_publicacao do objeto livro.
     *
     * @param data_publicacao é a data em que o livro foi publicado.
     */
    public void setData_publicacao(LocalDate data_publicacao) {
        this.data_publicacao = data_publicacao;
    }

    /**
     * Método que retorna o parâmetro data_inclusão do objeto livro.
     *
     * @return a data de inclusão do livro.
     */
    public Date getData_inclusao() {
        return data_inclusao;
    }

    /**
     * Método que define o valor do parâmetro data_inclusao do objeto livro.
     *
     * @param data_inclusao é a data em que o livro foi incluído em sistema. Só
     * será alterado uma vez na inclusão do produto no sistema (na tabela
     * livros, na realidade) é alterado de forma automática, por meio de um
     * gatilho na inserção do dado de um novo livro na tabela do banco de dados.
     */
    public void setData_inclusao(Date data_inclusao) {
        this.data_inclusao = data_inclusao;
    }

    /**
     * Método que retorna o parâmetro data_alteracao do objeto livro.
     *
     * @return a data da última alteração do livro
     */
    public Date getData_alteracao() {
        return data_alteracao;
    }

    /**
     * Método que define o valor do parâmetro data_alteracao do objeto livro.
     *
     * @param data_alteracao que é a data da última alteração do livro.
     */
    public void setData_alteracao(Date data_alteracao) {
        this.data_alteracao = data_alteracao;
    }

    /**
     * @return Dados do objeto Livro em forma de String
     */
    //Método toString para facilitar a visualização dos dados, optei pelo .format para ficar mais fácil a visualização no código. Basicamente apresentará os dados do Livro em forma de String, visando facilitar a apresentação em relatórios e afins.
    @Override
    public String toString() {
        return String.format("Livro [etiqueta_livro= %d, titulo= %s, autor= %s, editora= %s, genero= %s, isbn= %s, data_publicacao= %s, data_inclusao= %s, data_alteracao= %s",
                etiqueta_livro, titulo, autor, editora, genero, isbn, data_publicacao, data_inclusao, data_alteracao);

    }
}
