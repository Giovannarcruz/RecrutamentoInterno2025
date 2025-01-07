package giovanna.projeto.livraria1.util;

import giovanna.projeto.livraria1.model.Doc;
import giovanna.projeto.livraria1.model.Livro;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import static org.hibernate.query.sqm.SqmTreeTransformationLogger.LOGGER;
import org.jboss.logging.Logger;

/**
 *
 * @author giova
 */
public class ConverterLivro {

    /**
     * Converte um objeto Doc da API Open Library em um objeto Livro.
     *
     * @param doc O objeto Doc retornado pela API Open Library.
     * @return Um objeto Livro pronto para ser usado no sistema.
     */
    public static Livro fromOpenLibraryDoc(Doc doc) {
        Livro livro = new Livro();

        livro.setTitulo(doc.getTitle() != null ? doc.getTitle() : "Título não disponível");
        livro.setAutor(doc.getPrimaryAuthor() != null ? doc.getPrimaryAuthor() : "Autor não disponível");
        livro.setEditora(doc.getPrimaryPublisher() != null ? doc.getPrimaryPublisher() : "Editora não disponível");
        livro.setGenero(doc.getPrimarySubject() != null ? doc.getPrimarySubject() : "Gênero não disponível");
        // Tratamento da data de publicação
        List<String> publishDates = doc.getPublishDate();
        if (publishDates != null && !publishDates.isEmpty()) {
            String rawDate = publishDates.get(0); // Pega a primeira data disponível
            livro.setData_publicacao(parseDataPublicacao(rawDate));
        }

        // Preencha o ISBN com o primeiro disponível, caso exista
        livro.setIsbn(doc.getPrimaryIsbn() != null ? doc.getPrimaryIsbn() : "");

        return livro;
    }
    /**
     * Método para formatar a data originada da integração
     * @param rawDate data antes da formatação
     * @return data formatada;
     */
    private static LocalDate parseDataPublicacao(String rawDate) {
        try {
            // Tenta converter para uma data completa no formato "Jul 16, 2005"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            return LocalDate.parse(rawDate, formatter);
        } catch (DateTimeParseException e1) {
            try {
                // Tenta converter apenas o ano (e.g., "2005")
                return LocalDate.of(Integer.parseInt(rawDate), 1, 1); // Assume 1º de janeiro
            } catch (NumberFormatException e2) {
                // Retorna null se nenhuma conversão funcionar
               LOGGER.log(Logger.Level.WARN, "Formato de data inválido: " + rawDate);
                return null;
            }
        }
    }

    /**
     * Converte um objeto Livro para uma String JSON (opcional). Pode ser útil
     * para enviar dados para outras APIs ou para debug.
     *
     * @param livro O objeto Livro a ser convertido.
     * @return A representação JSON do Livro.
     */
    public static String toJson(Livro livro) {
        return ""; // Placeholder
    }
}
