package giovanna.projeto.livraria1.view;

import giovanna.projeto.livraria1.model.Genero;
import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.GeneroService;
import giovanna.projeto.livraria1.services.LivroService;
import giovanna.projeto.livraria1.services.RelatorioService;
import giovanna.projeto.livraria1.util.AutoComplete;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;

/**
 * Classe responsável por exibir relatórios de livros filtrados por gênero.
 * Permite visualizar os resultados em uma tabela e salvar o relatório em formato XLS.
 * <p>
 * Esta classe fornece uma interface para que o usuário possa filtrar livros por gênero,
 * atualizar os resultados na tabela e salvar o relatório gerado em formato XLS.
 * </p>
 */
public class TelaRelatorioGenero extends JPanel {
    /**
     * Variáveis utilizadas para a classe em questão.
     */
    private final JTextField txtGenero; // Campo para entrada de gêneros
    private final JButton btnAtualizar, btnSalvar; // Botões para atualizar e salvar relatório
    private final JTable tabelaRelatorio; // Tabela para exibição dos dados
    private final DefaultTableModel modeloTabela; // Modelo da tabela
    private List<Genero> generos; // Lista de gêneros para autocomplete
    private final JPopupMenu popupGenero = new JPopupMenu(); // Popup para autocomplete

     /**
     * Construtor para inicializar o painel de relatório.
     * Configura os componentes visuais e as ações associadas.
     */
    public TelaRelatorioGenero() {
        setLayout(new BorderLayout());

        // Painel superior com campo de texto e botões
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtGenero = new JTextField(30);
        btnAtualizar = new JButton("Atualizar");
        btnSalvar = new JButton("Salvar");

        // Adiciona ações aos botões
        btnAtualizar.addActionListener(this::atualizarRelatorio);
        btnSalvar.addActionListener(this::salvarRelatorio);

        // Adiciona componentes ao painel superior
        topPanel.add(new JLabel("Gênero:"));
        topPanel.add(txtGenero);
        topPanel.add(btnAtualizar);
        topPanel.add(btnSalvar);
        add(topPanel, BorderLayout.NORTH);

        // Configuração da tabela de resultados
        modeloTabela = new DefaultTableModel(
                new String[]{"Título", "Autor", "Gênero", "ISBN", "Editora", "Data Publicação"}, 0
        );
        tabelaRelatorio = new JTable(modeloTabela);
        add(new JScrollPane(tabelaRelatorio), BorderLayout.CENTER);

        // Configura o autocomplete para o campo de gênero
        configurarAutocompletarGenero();
    }

    /**
     * Atualiza os dados da tabela com base nos gêneros informados.
     * 
     * @param e Evento de ação disparado pelo botão "Atualizar".
     */
    private void atualizarRelatorio(ActionEvent e) {
        String generosTexto = txtGenero.getText().trim();
        if (generosTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe pelo menos um gênero para filtrar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LivroService livroService = new LivroService();
            List<Livro> livros = livroService.buscarLivrosPorGeneros(generosTexto.split(",")); // Buscar livros pelo gênero
            atualizarTabela(livros); // Atualiza a tabela com os livros encontrados
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Salva o relatório em formato XLS com base nos dados exibidos na tabela.
     *
     * @param e Evento de ação disparado pelo botão "Salvar".
     */
    private void salvarRelatorio(ActionEvent e) {
        String generosTexto = txtGenero.getText().trim();
        if (generosTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe pelo menos um gênero para salvar o relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                RelatorioService relatorioService = new RelatorioService();

                List<Livro> livros = new ArrayList<>();
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    Livro livro = new Livro();
                    livro.setTitulo((String) modeloTabela.getValueAt(i, 0));
                    livro.setAutor((String) modeloTabela.getValueAt(i, 1));
                    livro.setGenero((String) modeloTabela.getValueAt(i, 2));
                    livro.setIsbn((String) modeloTabela.getValueAt(i, 3));
                    livro.setEditora((String) modeloTabela.getValueAt(i, 4));

                    Object dataObj = modeloTabela.getValueAt(i, 5);
                    if (dataObj instanceof java.sql.Date) {
                        livro.setData_publicacao(((Date) dataObj).toLocalDate());
                    }

                    livros.add(livro); // Adiciona o livro à lista
                }

                // Salva o relatório no formato XLS
                relatorioService.gerarRelatorioPorGenero(generosTexto.split(","), "xls", filePath);

                JOptionPane.showMessageDialog(this, "Relatório salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HeadlessException | IOException | SQLException | JRException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atualiza os dados da tabela com a lista de livros fornecida.
     *
     * @param livros Lista de livros a ser exibida na tabela.
     */
    private void atualizarTabela(List<Livro> livros) {
        modeloTabela.setRowCount(0); // Limpa os dados existentes na tabela
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getGenero(),
                    livro.getIsbn(),
                    livro.getEditora(),
                    livro.getData_publicacao() // Exibe LocalDate diretamente
            });
        }
    }

    /**
     * Configura o campo de gênero para suporte a autocomplete.
     */
    private void configurarAutocompletarGenero() {
        try {
            GeneroService generoService = new GeneroService();
            this.generos = generoService.listaGeneros(); // Obtém a lista de gêneros do banco de dados
            if (this.generos != null) {
                AutoComplete.configureAutoComplete(txtGenero, popupGenero, this.generos, Genero::getNome); // Configura o autocompletar no campo de gênero
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gêneros: " + ex.getMessage());
        }
    }
}
