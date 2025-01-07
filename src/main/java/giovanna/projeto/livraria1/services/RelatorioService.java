package giovanna.projeto.livraria1.services;

import giovanna.projeto.livraria1.model.Livro;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Classe responsável pela geração de relatórios de livros por gênero.
 * Suporta exportação em formatos PDF e Excel (XLSX).
 */
public class RelatorioService {

    /**
     * Gera um relatório de livros filtrados por gênero utilizando JasperReports.
     *
     * @param generos       Array de gêneros para filtrar os livros.
     * @param formatoSaida  Formato do relatório ("pdf" ou "xls").
     * @param caminhoSalvar Caminho para salvar o arquivo gerado.
     * @throws SQLException Se ocorrer erro ao buscar livros no banco de dados.
     * @throws JRException  Se ocorrer erro ao gerar o relatório com JasperReports.
     * @throws IOException  Se ocorrer erro ao salvar o relatório no disco.
     */
    public void gerarRelatorioPorGenero(String[] generos, String formatoSaida, String caminhoSalvar)
            throws SQLException, JRException, IOException {

        // Caminho para o template do relatório Jasper
        String caminhoRelatorio = "C:/Users/giova/Documents/Livraria1/src/main/java/giovanna/projeto/livraria1/resources/relatorios/LivroporGenero.jasper";

        // Serviço para buscar livros filtrados
        LivroService livroService = new LivroService();
        List<Livro> livros = livroService.buscarLivrosPorGeneros(generos);

        // Fonte de dados para JasperReports
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(livros);

        // Parâmetros adicionais para o relatório
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "Relatório de Livros por Gênero");
        parametros.put("GENEROS_FILTRADOS", String.join(", ", generos));

        // Preencher o relatório com os dados e parâmetros
        JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoRelatorio, parametros, dataSource);

        // Exportar o relatório no formato desejado
        if ("xls".equalsIgnoreCase(formatoSaida)) {
            exportarRelatorioParaExcel(livros, caminhoSalvar); // Passando a lista de livros diretamente
        } else {
            throw new IllegalArgumentException("Formato de saída inválido. Deve ser '.xls");
        }
    }
    /**
     * Exporta um relatório para o formato Excel usando Apache POI.
     *
     * @param livros        Lista de livros para gerar o relatório.
     * @param caminhoSalvar Caminho para salvar o arquivo Excel.
     * @throws JRException Se ocorrer erro durante a exportação.
     */
    private void exportarRelatorioParaExcel(List<Livro> livros, String caminhoSalvar) throws JRException {
        // Usando Apache POI diretamente para exportação para Excel
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Livros");

            // Cria o cabeçalho da tabela
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Título", "Autor", "Gênero", "ISBN", "Editora", "Data Publicação"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                // Estilo opcional para cabeçalho
                CellStyle headerStyle = workbook.createCellStyle();
                XSSFFont font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            // Adiciona os dados dos livros
            int rowNum = 1;
            for (Livro livro : livros) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(livro.getTitulo());
                row.createCell(1).setCellValue(livro.getAutor());
                row.createCell(2).setCellValue(livro.getGenero());
                row.createCell(3).setCellValue(livro.getIsbn());
                row.createCell(4).setCellValue(livro.getEditora());
                if (livro.getData_publicacao() != null) {
                    row.createCell(5).setCellValue(livro.getData_publicacao().toString());
                }
            }

            // Salva o arquivo Excel no disco
            try (FileOutputStream fileOut = new FileOutputStream(caminhoSalvar)) {
                workbook.write(fileOut);
            }
            JOptionPane.showMessageDialog(null, "Relatório salvo em Excel no caminho: " + caminhoSalvar);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório Excel: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
