package giovanna.projeto.livraria1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import giovanna.projeto.livraria1.model.Doc;
import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.model.OpenLibraryBook;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giova
 */
public class ISBNApiClient {

    private static final Logger LOGGER = Logger.getLogger(ISBNApiClient.class.getName());
    private static final String API_URL = "https://openlibrary.org/search.json?isbn=";

    /**
     * Busca informações de um livro na API Open Library pelo ISBN.
     *
     * @param isbn O ISBN do livro a ser buscado.
     * @return Um objeto Livro preenchido com os dados da API, ou null se não
     * encontrado.
     */
public static Livro buscarLivroPorISBN(String isbn) {
    try {
        // Formata a URL com o ISBN fornecido pelo usuário
        URL url = new URL(API_URL + isbn);
        LOGGER.info("URL Gerada: " + url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }

                // Processa a resposta JSON
                ObjectMapper mapper = new ObjectMapper();
                OpenLibraryBook openLibraryBook = mapper.readValue(jsonResponse.toString(), OpenLibraryBook.class);

                // Verifica se há documentos retornados
                if (openLibraryBook.getDocs() == null || openLibraryBook.getDocs().isEmpty()) {
                    LOGGER.warning("Nenhum documento encontrado para o ISBN: " + isbn);
                    return null;
                }

                // Itera pelos documentos para localizar o ISBN-13 fornecido
                for (Doc doc : openLibraryBook.getDocs()) {
                    List<String> isbns = doc.getIsbn(); // Obtém a lista de ISBNs do documento
                    if (isbns != null && isbns.contains(isbn)) {
                        LOGGER.info("Documento correspondente ao ISBN encontrado.");
                        Livro livro = ConverterLivro.fromOpenLibraryDoc(doc);
                        livro.setIsbn(isbn); // Garante que o ISBN usado seja o correto
                        return livro;
                    }
                }

                LOGGER.warning("Nenhum documento com o ISBN exato foi encontrado.");
                return null;
            }
        } else {
            LOGGER.warning("Erro na requisição para o ISBN: " + isbn + " - Código de resposta: " + connection.getResponseCode());
        }
    } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Erro ao buscar livro pelo ISBN: " + isbn, e);
    }

    return null;
}

    /**
     * Método para preencher a data, que na requisição vem como somente o ano de publicação
     * @param data data obtida via api
     * @return data formatada
     */
     private static String formatarDataPublicacao(String data) {
        if (data.matches("\\d{4}")) {
            return "01 de janeiro de " + data;
        }
        return data;
    }

}
