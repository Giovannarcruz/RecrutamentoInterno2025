package giovanna.projeto.livraria1.model;

/**
 * A classe <code>LivroSimilares</code> representa a relação entre dois livros
 * que são considerados similares. Cada instância dessa classe contém
 * as etiquetas de dois livros, sendo um livro principal e o outro similar.
 * 
 * Esta classe mapeia uma tabela intermediária no banco de dados para
 * associar livros similares.
 * 
 * @author Giovanna
 */
public class LivroSimilares {

    // Etiqueta do livro principal (referência ao livro original)
    private int etiqueta_livro;

    // Etiqueta do livro similar (referência ao livro considerado similar)
    private int etiqueta_similar;

    /**
     * Construtor com parâmetros, utilizado para criar uma instância de LivroSimilares
     * associando dois livros pela sua etiqueta.
     * 
     * @param etiqueta_livro A etiqueta do livro principal.
     * @param etiqueta_similar A etiqueta do livro similar.
     */
    public LivroSimilares(int etiqueta_livro, int etiqueta_similar) {
        this.etiqueta_livro = etiqueta_livro;
        this.etiqueta_similar = etiqueta_similar;
    }

    /**
     * Retorna a etiqueta do livro principal.
     * 
     * @return A etiqueta do livro principal.
     */
    public int getEtiqueta_livro() {
        return etiqueta_livro;
    }

    /**
     * Define a etiqueta do livro principal.
     * 
     * @param etiqueta_livro A nova etiqueta do livro principal.
     */
    public void setEtiqueta_livro(int etiqueta_livro) {
        this.etiqueta_livro = etiqueta_livro;
    }

    /**
     * Retorna a etiqueta do livro similar.
     * 
     * @return A etiqueta do livro similar.
     */
    public int getEtiqueta_similar() {
        return etiqueta_similar;
    }

    /**
     * Define a etiqueta do livro similar.
     * 
     * @param etiqueta_similar A nova etiqueta do livro similar.
     */
    public void setEtiqueta_similar(int etiqueta_similar) {
        this.etiqueta_similar = etiqueta_similar;
    }

}
