
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
    private final Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     *
     * @return
     */
    @JsonProperty("numFound")
    public Integer getNumFound() {
        return numFound;
    }

    /**
     *
     * @param numFound
     */
    @JsonProperty("numFound")
    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    /**
     *
     * @return
     */
    @JsonProperty("start")
    public Integer getStart() {
        return start;
    }

    /**
     *
     * @param start
     */
    @JsonProperty("start")
    public void setStart(Integer start) {
        this.start = start;
    }

    /**
     *
     * @return
     */
    @JsonProperty("numFoundExact")
    public Boolean getNumFoundExact() {
        return numFoundExact;
    }

    /**
     *
     * @param numFoundExact
     */
    @JsonProperty("numFoundExact")
    public void setNumFoundExact(Boolean numFoundExact) {
        this.numFoundExact = numFoundExact;
    }

    /**
     *
     * @return docs
     */
    @JsonProperty("docs")
    public List<Doc> getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     */
    @JsonProperty("docs")
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    /**
     *
     * @return
     */
    @JsonProperty("isbn")
    public String getIsbn() {
        return isbn;
    }

    /**
     *
     * @param isbn
     */
    @JsonProperty("isbn")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     *
     * @return
     */
    @JsonProperty("offset")
    public Object getOffset() {
        return offset;
    }

    /**
     *
     * @param offset
     */
    @JsonProperty("offset")
    public void setOffset(Object offset) {
        this.offset = offset;
    }

    /**
     *
     * @return
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     *
     * @param name
     * @param value
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    /**
     * Retorna o primeiro documento da lista de docs, se existir.
     * @return 
     */
    public Optional<Doc> getFirstDoc() {
        return docs != null && !docs.isEmpty() ? Optional.of(docs.get(0)) : Optional.empty();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpenLibraryBook.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("numFound");
        sb.append('=');
        sb.append(((this.numFound == null)?"<null>":this.numFound));
        sb.append(',');
        sb.append("start");
        sb.append('=');
        sb.append(((this.start == null)?"<null>":this.start));
        sb.append(',');
        sb.append("numFoundExact");
        sb.append('=');
        sb.append(((this.numFoundExact == null)?"<null>":this.numFoundExact));
        sb.append(',');
        sb.append("docs");
        sb.append('=');
        sb.append(((this.docs == null)?"<null>":this.docs));
        sb.append(',');
        sb.append("isbn");
        sb.append('=');
        sb.append(((this.isbn == null)?"<null>":this.isbn));
        sb.append(',');
        sb.append("offset");
        sb.append('=');
        sb.append(((this.offset == null)?"<null>":this.offset));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
