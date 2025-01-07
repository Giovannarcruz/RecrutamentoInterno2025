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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.hibernate.query.sqm.SqmTreeTransformationLogger.LOGGER;
import org.jboss.logging.Logger;

/**
 * Representa um documento (por exemplo, um livro) com informações sobre o autor,
 * título, data de publicação, editor, assunto e ISBN.
 * A classe é utilizada para mapear dados de um formato JSON.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "author_name",
    "publish_date",
    "publisher",
    "title",
    "subject",
    "isbn"
})
@Generated("jsonschema2pojo")
public class Doc {

    @JsonProperty("author_name")
    private List<String> authorName; // Lista de nomes de autores
    @JsonProperty("publish_date")
    private List<String> publishDate; // Lista de datas de publicação
    @JsonProperty("publisher")
    private List<String> publisher; // Lista de editores
    @JsonProperty("title")
    private String title; // Título do documento
    @JsonProperty("subject")
    private List<String> subject; // Lista de assuntos ou gêneros
    @JsonProperty("isbn")
    private List<String> isbn; // Lista de ISBNs associados ao documento

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<>(); // Propriedades adicionais

    /**
     * Obtém a lista de nomes de autores do documento.
     * 
     * @return Lista de nomes de autores.
     */
    @JsonProperty("author_name")
    public List<String> getAuthorName() {
        return authorName;
    }

    /**
     * Define a lista de nomes de autores do documento.
     * 
     * @param authorName Lista de nomes de autores.
     */
    @JsonProperty("author_name")
    public void setAuthorName(List<String> authorName) {
        this.authorName = authorName;
    }

    /**
     * Obtém a lista de datas de publicação do documento.
     * 
     * @return Lista de datas de publicação.
     */
    @JsonProperty("publish_date")
    public List<String> getPublishDate() {
        return publishDate;
    }

    /**
     * Define a lista de datas de publicação do documento.
     * 
     * @param publishDate Lista de datas de publicação.
     */
    @JsonProperty("publish_date")
    public void setPublishDate(List<String> publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Obtém a lista de editores do documento.
     * 
     * @return Lista de editores.
     */
    @JsonProperty("publisher")
    public List<String> getPublisher() {
        return publisher;
    }

    /**
     * Define a lista de editores do documento.
     * 
     * @param publisher Lista de editores.
     */
    @JsonProperty("publisher")
    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    /**
     * Obtém o título do documento.
     * 
     * @return Título do documento.
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Define o título do documento.
     * 
     * @param title Título do documento.
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtém a lista de assuntos ou gêneros relacionados ao documento.
     * 
     * @return Lista de assuntos.
     */
    @JsonProperty("subject")
    public List<String> getSubject() {
        return subject;
    }

    /**
     * Define a lista de assuntos ou gêneros relacionados ao documento.
     * 
     * @param subject Lista de assuntos.
     */
    @JsonProperty("subject")
    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    /**
     * Obtém a lista de ISBNs associados ao documento.
     * 
     * @return Lista de ISBNs.
     */
    @JsonProperty("isbn")
    public List<String> getIsbn() {
        return isbn;
    }

    /**
     * Define a lista de ISBNs associados ao documento.
     * 
     * @param isbn Lista de ISBNs.
     */
    @JsonProperty("isbn")
    public void setIsbn(List<String> isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtém as propriedades adicionais não mapeadas no objeto.
     * 
     * @return Mapa de propriedades adicionais.
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Define uma propriedade adicional não mapeada.
     * 
     * @param name Nome da propriedade.
     * @param value Valor da propriedade.
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /**
     * Retorna o primeiro ISBN disponível na lista de ISBNs (se houver).
     * 
     * @return Um ISBN como String ou null caso não haja nenhum.
     */
    @JsonIgnore
    public String getPrimaryIsbn() {
        if (isbn != null && !isbn.isEmpty()) {
            return isbn.get(0); // Retorna o primeiro ISBN da lista
        }
        return null;
    }

    /**
     * Retorna o nome do primeiro autor, ou null se não houver.
     * 
     * @return Nome do primeiro autor ou null.
     */
    public String getPrimaryAuthor() {
        return authorName != null && !authorName.isEmpty() ? authorName.get(0) : null;
    }

    /**
     * Retorna o nome do primeiro editor, ou null se não houver.
     * 
     * @return Nome do primeiro editor ou null.
     */
    public String getPrimaryPublisher() {
        return publisher != null && !publisher.isEmpty() ? publisher.get(0) : null;
    }

    /**
     * Retorna a primeira data de publicação formatada.
     * Caso o formato seja apenas o ano, a data será configurada para o primeiro dia de janeiro desse ano.
     * Caso o formato seja dd/MM/yyyy, a data será parseada corretamente.
     * 
     * @return A data de publicação formatada, ou null caso não seja possível formatar.
     */
    public LocalDate getFormattedPublishDate() {
        if (publishDate != null && !publishDate.isEmpty()) {
            String rawDate = publishDate.get(0);
            try {
                if (rawDate.matches("\\d{4}")) {
                    return LocalDate.of(Integer.parseInt(rawDate), 1, 1); // Ano simples
                } else if (rawDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    return LocalDate.parse(rawDate, formatter); // Data completa
                }
            } catch (NumberFormatException e) {
            LOGGER.log(Logger.Level.ERROR, "Data inválida", e);
            }
        }
        return null;
    }

    /**
     * Retorna o primeiro gênero (assunto) da lista, ou null caso não haja.
     * 
     * @return O primeiro gênero ou null.
     */
    public String getPrimarySubject() {
        return subject != null && !subject.isEmpty() ? subject.get(0) : null;
    }

    /**
     * Gera uma representação em String do objeto, incluindo todos os atributos e suas propriedades.
     * 
     * @return Representação em String do objeto.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Doc.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("authorName");
        sb.append('=');
        sb.append(((this.authorName == null) ? "<null>" : this.authorName));
        sb.append(',');
        sb.append("publishDate");
        sb.append('=');
        sb.append(((this.publishDate == null) ? "<null>" : this.publishDate));
        sb.append(',');
        sb.append("publisher");
        sb.append('=');
        sb.append(((this.publisher == null) ? "<null>" : this.publisher));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("subject");
        sb.append('=');
        sb.append(((this.subject == null) ? "<null>" : this.subject));
        sb.append(',');
        sb.append("isbn");
        sb.append('=');
        sb.append(((this.isbn == null) ? "<null>" : this.isbn));
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
