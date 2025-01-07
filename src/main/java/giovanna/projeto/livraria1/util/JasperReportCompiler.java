package giovanna.projeto.livraria1.util;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giova
 */
public class JasperReportCompiler {
    
    private static final Logger LOGGER = Logger.getLogger(JasperReportCompiler.class.getName());

    /**
     * Compila o relatório Jasper a partir de um arquivo JRXML.
     * @param jrxmlPath Caminho para o arquivo JRXML.
     * @return O objeto JasperReport compilado.
     * @throws JRException Se ocorrer um erro na compilação do relatório.
     */
    public static JasperReport compileReport(String jrxmlPath) throws JRException {
        try {
            // Tenta compilar o arquivo JRXML
            return JasperCompileManager.compileReport(jrxmlPath);
        } catch (JRException e) {
            // Se ocorrer erro na compilação, registra o erro e lança a exceção
            LOGGER.log(Level.SEVERE, "Erro ao compilar o relatório Jasper: " + jrxmlPath, e);
            throw new JRException("Erro ao compilar o relatório Jasper", e);
        }
    }
}
