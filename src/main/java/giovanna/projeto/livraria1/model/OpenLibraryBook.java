package giovanna.projeto.livraria1.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Optional;

/**
 * Representa a estrutura de resposta retornada pela API Open Library ao consultar livros.
 * Contém informações como número total de resultados, documentos retornados (livros), e outros metadados.
 * Esta classe é gerada a partir de um esquema JSON.
 * 
 * @author Giovanna
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "numFound",
    "start",
    "numFoundExact",
    "docs",
    "q",
    "offset"
})
@Generated("jsonschema2pojo")
public class OpenLibraryBook {

    @JsonProperty("numFound")
    private Integer numFound;
    @JsonProperty("start")
    private Integer start;
    @JsonProperty("numFoundExact")
    private Boolean numFoundExact;
    @JsonProperty("docs")
    private List<Doc> docs;
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("offset")
    private Object offset;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<>();

    /**
     * Obtém o número total de resultados encontrados pela API.
     *
     * @return O número total de resultados encontrados.
     */
    @JsonProperty("numFound")
    public Integer getNumFound() {
        return numFound;
    }

    /**
     * Define o número total de resultados encontrados pela API.
     *
     * @param numFound O número de resultados.
     */
    @JsonProperty("numFound")
    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    /**
     * Obtém o índice de início dos resultados retornados.
     *
     * @return O índice inicial dos resultados.
     */
    @JsonProperty("start")
    public Integer getStart() {
        return start;
    }

    /**
     * Define o índice de início dos resultados retornados.
     *
     * @param start O índice inicial.
     */
    @JsonProperty("start")
    public void setStart(Integer start) {
        this.start = start;
    }

    /**
     * Indica se o número de resultados encontrados é exato.
     *
     * @return {@code true} se o número de resultados é exato; {@code false} caso contrário.
     */
    @JsonProperty("numFoundExact")
    public Boolean getNumFoundExact() {
        return numFoundExact;
    }

    /**
     * Define se o número de resultados encontrados é exato.
     *
     * @param numFoundExact {@code true} para resultados exatos; {@code false} caso contrário.
     */
    @JsonProperty("numFoundExact")
    public void setNumFoundExact(Boolean numFoundExact) {
        this.numFoundExact = numFoundExact;
    }

    /**
     * Obtém a lista de documentos (livros) retornados pela API.
     *
     * @return Uma lista de documentos representando os livros encontrados.
     */
    @JsonProperty("docs")
    public List<Doc> getDocs() {
        return docs;
    }

    /**
     * Define a lista de documentos (livros) retornados pela API.
     *
     * @param docs Uma lista de objetos {@link Doc}.
     */
    @JsonProperty("docs")
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    /**
     * Obtém o ISBN do livro, se disponível.
     *
     * @return O ISBN do livro.
     */
    @JsonProperty("isbn")
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define o ISBN do livro.
     *
     * @param isbn O ISBN do livro.
     */
    @JsonProperty("isbn")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtém o valor do campo "offset" retornado pela API.
     *
     * @return O valor do offset.
     */
    @JsonProperty("offset")
    public Object getOffset() {
        return offset;
    }

    /**
     * Define o valor do campo "offset" retornado pela API.
     *
     * @param offset O valor do offset.
     */
    @JsonProperty("offset")
    public void setOffset(Object offset) {
        this.offset = offset;
    }

    /**
     * Obtém propriedades adicionais não mapeadas explicitamente nos campos desta classe.
     *
     * @return Um mapa de propriedades adicionais.
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Adiciona uma propriedade adicional não mapeada explicitamente nos campos desta classe.
     *
     * @param name O nome da propriedade.
     * @param value O valor da propriedade.
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /**
     * Obtém o primeiro documento da lista de documentos retornados.
     *
     * @return Um {@link Optional} contendo o primeiro {@link Doc}, ou vazio se a lista estiver vazia.
     */
    public Optional<Doc> getFirstDoc() {
        return docs != null && !docs.isEmpty() ? Optional.of(docs.get(0)) : Optional.empty();
    }

    /**
     * Retorna uma representação em string dos dados armazenados nesta classe.
     *
     * @return Uma string representando os valores armazenados.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpenLibraryBook.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("numFound");
        sb.append('=');
        sb.append(((this.numFound == null) ? "<null>" : this.numFound));
        sb.append(',');
        sb.append("start");
        sb.append('=');
        sb.append(((this.start == null) ? "<null>" : this.start));
        sb.append(',');
        sb.append("numFoundExact");
        sb.append('=');
        sb.append(((this.numFoundExact == null) ? "<null>" : this.numFoundExact));
        sb.append(',');
        sb.append("docs");
        sb.append('=');
        sb.append(((this.docs == null) ? "<null>" : this.docs));
        sb.append(',');
        sb.append("isbn");
        sb.append('=');
        sb.append(((this.isbn == null) ? "<null>" : this.isbn));
        sb.append(',');
        sb.append("offset");
        sb.append('=');
        sb.append(((this.offset == null) ? "<null>" : this.offset));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
