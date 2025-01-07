package giovanna.projeto.livraria1.view;

import com.toedter.calendar.JDateChooser; // Importe o JDateChooser
import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.LivroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe para exibição e filtro de livros em uma tabela.
 * Permite a aplicação de filtros como título, autor, gênero, ISBN e data de publicação.
 */
public class TelaFiltrarLivros extends JPanel {

    // Campos para entrada de dados de filtro
    private final JTextField txtTitulo = new JTextField(15); // Campo de entrada para título
    private final JTextField txtAutor = new JTextField(15); // Campo de entrada para autor
    private final JTextField txtGenero = new JTextField(15); // Campo de entrada para gênero
    private final JTextField txtISBN = new JTextField(15); // Campo de entrada para ISBN
    private final JDateChooser dateChooserDataPublicacao = new JDateChooser(); // Componente para selecionar data

    // Botões para aplicar e limpar filtros
    private final JButton btnAplicarFiltros = new JButton("Aplicar Filtros"); 
    private final JButton btnLimparFiltros = new JButton("Limpar Filtros");

    // Modelo e tabela para exibição dos resultados dos filtros
    private final DefaultTableModel modeloTabela; 
    private final JTable tabelaLivros; 

    // Serviço para acesso aos livros no banco de dados
    private final LivroService livroService; 

    // Formato de exibição da data para a tabela
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor da classe TelaFiltrarLivros.
     * Configura os componentes visuais e a lógica associada.
     *
     * @throws SQLException caso ocorra um erro ao acessar o banco de dados.
     */
    public TelaFiltrarLivros() throws SQLException {
        // Inicializa o serviço de livros e o layout da tela
        this.livroService = new LivroService();
        setLayout(new BorderLayout());

        // Criação e configuração do painel de filtros
        JPanel panelFiltros = criarPainelFiltros();
        add(panelFiltros, BorderLayout.NORTH);

        // Configuração da tabela para exibição dos resultados
        modeloTabela = new DefaultTableModel(
                new String[]{"Etiqueta", "Título", "Autor", "Editora", "Gênero", "ISBN", "Data Publicação"}, 0
        );
        tabelaLivros = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões: aplicar filtros e limpar filtros
        btnAplicarFiltros.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> {
            try {
                limparFiltros();
            } catch (SQLException ex) {
                Logger.getLogger(TelaFiltrarLivros.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Carrega todos os livros no início
        carregarLivros(null, null, null, null, null);
    }

    /**
     * Cria um painel com os filtros de pesquisa.
     *
     * @return JPanel configurado com os campos de filtro.
     */
    private JPanel criarPainelFiltros() {
        JPanel panelFiltros = new JPanel(new GridLayout(2, 5, 5, 5));
        panelFiltros.add(criarFiltroPanel("Título:", txtTitulo));
        panelFiltros.add(criarFiltroPanel("Autor:", txtAutor));
        panelFiltros.add(criarFiltroPanel("Gênero:", txtGenero));
        panelFiltros.add(criarFiltroPanel("ISBN:", txtISBN));
        panelFiltros.add(criarFiltroPanel("Data Publicação:", dateChooserDataPublicacao));

        // Painel para os botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotoes.add(btnAplicarFiltros);
        panelBotoes.add(btnLimparFiltros);

        // Painel principal que contém filtros e botões
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelFiltros, BorderLayout.CENTER);
        mainPanel.add(panelBotoes, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Cria um painel para um filtro específico de texto.
     *
     * @param labelText   Texto do rótulo do filtro.
     * @param textField   Campo de texto do filtro.
     * @return JPanel contendo o rótulo e o campo de entrada.
     */
    private JPanel criarFiltroPanel(String labelText, JTextField textField) {
        JPanel filtroPanel = new JPanel(new GridLayout(2, 1));
        filtroPanel.add(new JLabel(labelText));
        filtroPanel.add(textField);
        return filtroPanel;
    }

    /**
     * Cria um painel para um filtro específico com um seletor de data.
     *
     * @param labelText   Texto do rótulo do filtro.
     * @param dateChooser Componente de seleção de data.
     * @return JPanel contendo o rótulo e o seletor de data.
     */
    private JPanel criarFiltroPanel(String labelText, JDateChooser dateChooser) {
        JPanel filtroPanel = new JPanel(new GridLayout(2, 1));
        filtroPanel.add(new JLabel(labelText));
        filtroPanel.add(dateChooser);
        return filtroPanel;
    }

    /**
     * Aplica os filtros fornecidos e atualiza a tabela com os resultados.
     */
    private void aplicarFiltros() {
        try {
            // Obtém os valores dos filtros
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String genero = txtGenero.getText().trim();
            String isbn = txtISBN.getText().trim();
            Date dataPublicacaoDate = dateChooserDataPublicacao.getDate();
            LocalDate dataPublicacao = dataPublicacaoDate != null
                    ? dataPublicacaoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null;

            // Chama o método para carregar livros com os filtros aplicados
            carregarLivros(
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    genero.isEmpty() ? null : genero,
                    isbn.isEmpty() ? null : isbn,
                    dataPublicacao
            );
        } catch (SQLException ex) {
            // Exibe mensagem de erro caso falhe ao aplicar os filtros
            JOptionPane.showMessageDialog(this, "Erro ao aplicar filtros: " + ex.getMessage());
            Logger.getLogger(TelaFiltrarLivros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpa todos os filtros e recarrega a tabela com todos os livros.
     *
     * @throws SQLException caso ocorra um erro ao acessar o banco de dados.
     */
    private void limparFiltros() throws SQLException {
        // Limpa os campos de filtro
        txtTitulo.setText("");
        txtAutor.setText("");
        txtGenero.setText("");
        txtISBN.setText("");
        dateChooserDataPublicacao.setDate(null);

        // Recarrega todos os livros na tabela
        carregarLivros(null, null, null, null, null);
    }

    /**
     * Carrega os livros com base nos filtros fornecidos e atualiza a tabela.
     *
     * @param titulo         Filtro para o título.
     * @param autor          Filtro para o autor.
     * @param genero         Filtro para o gênero.
     * @param isbn           Filtro para o ISBN.
     * @param data_publicacao Filtro para a data de publicação.
     * @throws SQLException caso ocorra um erro ao acessar o banco de dados.
     */
    private void carregarLivros(String titulo, String autor, String genero, String isbn, LocalDate data_publicacao) throws SQLException {
        // Obtém os livros filtrados através do serviço
        List<Livro> livros = livroService.filtrarLivros(titulo, autor, genero, isbn, data_publicacao);
        
        // Limpa a tabela antes de adicionar novos resultados
        modeloTabela.setRowCount(0);
        
        // Adiciona cada livro à tabela
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getEtiqueta_livro(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getEditora(),
                    livro.getGenero(),
                    livro.getIsbn(),
                    livro.getData_publicacao() != null
                            ? dateFormat.format(java.sql.Date.valueOf(livro.getData_publicacao()))
                            : "" // Formata a data na tabela
            });
        }
    }
}
