package giovanna.projeto.livraria1.view;

import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.LivroService;
import giovanna.projeto.livraria1.util.ISBNApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;

/**
 * Classe responsável pelo cadastro de livros, incluindo funcionalidades como busca,
 * edição, exclusão, e integração com a API OpenLibrary para cadastrar livros via ISBN.
 */
public class TelaCadastroLivros extends JPanel {

    private final DefaultTableModel modeloTabela; // Modelo da tabela de livros
    private final JTable tabelaLivros; // Tabela para exibir livros
    private final JPanel btnPanel; // Painel de botões de ação
    private LivroDialog livroDialog; // Diálogo de inclusão/edição de livros
    private static final Logger LOGGER = Logger.getLogger(TelaCadastroLivros.class.getName()); // Logger para captura de erros
    private JFrame parentFrame; // Referência ao frame pai
    private final JTextField txtPesquisa; // Campo de pesquisa
    private final JButton btnAtualizar; // Botão de atualização da tabela

    /**
     * Construtor da classe TelaCadastroLivros.
     * Inicializa a interface gráfica e os componentes necessários para o cadastro de livros.
     *
     * @throws ServiceException Caso ocorra um erro nos serviços utilizados.
     * @throws SQLException     Caso ocorra erro de conexão com o banco de dados.
     */
    public TelaCadastroLivros() throws ServiceException, SQLException {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout()); // Definindo o layout principal

        // Painel superior com botão de atualização
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtPesquisa = new JTextField(20); // Campo de pesquisa
        btnAtualizar = new JButton("Atualizar"); // Botão de atualizar tabela
        btnAtualizar.addActionListener(this::atualizarTabela); // Evento de clique no botão de atualizar

        topPanel.add(btnAtualizar); // Adiciona o botão de atualização no painel superior
        add(topPanel, BorderLayout.NORTH);

        // Configuração da tabela com cabeçalhos
        modeloTabela = new DefaultTableModel(
                new String[]{"Etiqueta", "ISBN", "Título", "Autores", "Editora", "Gênero", "Data Publicação"}, 0
        );
        tabelaLivros = new JTable(modeloTabela); // Tabela para exibir os livros
        JScrollPane scrollPane = new JScrollPane(tabelaLivros); // Adiciona a tabela ao scroll pane
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões de ação
        btnPanel = criarPainelBotoes(); // Cria painel com botões de ação
        add(btnPanel, BorderLayout.SOUTH);

        // Preenche a tabela com os livros cadastrados
        carregarLivros();
    }

    /**
     * Cria o painel com os botões de inclusão, edição, exclusão e cadastro via ISBN.
     *
     * @return Painel de botões configurado.
     */
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Botões de ação
        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnCadastrarISBN = new JButton("Cadastrar pelo ISBN");

        // Ações dos botões
        btnIncluir.addActionListener(e -> {
            try {
                abrirLivroDialog(null); // Abre o diálogo para inclusão de livro
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erro ao incluir livro", ex);
            }
        });

        btnEditar.addActionListener(e -> editarLivro()); // Ação de editar livro
        btnExcluir.addActionListener(e -> excluirLivro()); // Ação de excluir livro
        btnCadastrarISBN.addActionListener(e -> abrirDialogoISBN()); // Ação de cadastrar livro via ISBN

        // Adiciona os botões ao painel
        painel.add(btnIncluir);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnCadastrarISBN);
        
        return painel; // Retorna o painel com os botões configurados
    }

    /**
     * Carrega os livros cadastrados no banco de dados e atualiza a tabela.
     */
    private void carregarLivros() {
        try {
            LivroService service = new LivroService();
            List<Livro> livros = service.lista_livros(); // Obtém a lista de livros cadastrados
            atualizarModeloTabela(livros); // Atualiza a tabela com os livros obtidos
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar livros", ex);
        }
    }

    /**
     * Atualiza os dados exibidos na tabela.
     *
     * @param livros Lista de livros a serem exibidos.
     */
    private void atualizarModeloTabela(List<Livro> livros) {
        modeloTabela.setRowCount(0); // Limpa a tabela
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getEtiqueta_livro(),
                    livro.getIsbn(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getEditora(),
                    livro.getGenero(),
                    livro.getData_publicacao()
            });
        }
    }

    /**
     * Abre o diálogo para edição ou inclusão de um livro.
     *
     * @param livro Livro a ser editado ou null para inclusão.
     */
    private void abrirLivroDialog(Livro livro) throws SQLException {
        livroDialog = new LivroDialog(parentFrame, livro);
        livroDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarLivros(); // Atualiza a tabela ao fechar o diálogo
            }
        });
    }

    /**
     * Edita o livro selecionado na tabela.
     */
    private void editarLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow(); // Obtém a linha selecionada
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.");
            return;
        }

        int etiqueta = (int) modeloTabela.getValueAt(linhaSelecionada, 0); // Obtém a etiqueta do livro

        try {
            LivroService service = new LivroService();
            Livro livro = service.busca_porEtiquetaetiquetaLivro(etiqueta); // Busca o livro no banco
            if (livro != null) {
                abrirLivroDialog(livro); // Abre o diálogo de edição
            } else {
                JOptionPane.showMessageDialog(this, "Livro não encontrado.");
            }
        } catch (HeadlessException | SQLException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar livro: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao editar livro", ex);
        }
    }

    /**
     * Exclui o livro selecionado na tabela.
     */
    private void excluirLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow(); // Obtém a linha selecionada
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
            return;
        }

        int etiqueta = (int) modeloTabela.getValueAt(linhaSelecionada, 0); // Obtém a etiqueta do livro
        try {
            LivroService service = new LivroService();
            service.excluirLivro(etiqueta); // Exclui o livro
            carregarLivros(); // Atualiza a tabela
            JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
        } catch (HeadlessException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir livro: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao excluir livro", ex);
        }
    }

    /**
     * Abre o diálogo para cadastro de livro via ISBN.
     */
    private void abrirDialogoISBN() {
        String isbn = JOptionPane.showInputDialog(this, "Informe o ISBN-13 do livro:", "Cadastrar pelo ISBN", JOptionPane.PLAIN_MESSAGE);

        if (isbn != null && !isbn.trim().isEmpty()) {
            isbn = isbn.replaceAll("[^0-9]", ""); // Remove qualquer caractere não numérico
            if (isbn.length() == 13) {
                cadastrarLivroISBN(isbn); // Realiza o cadastro do livro via ISBN
            } else {
                JOptionPane.showMessageDialog(this, "ISBN inválido. Verifique se o mesmo contém 13 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.SEVERE, "ISBN inválido.");
            }
        }
    }

    /**
     * Realiza o cadastro ou atualização de um livro usando a API OpenLibrary.
     *
     * @param isbn ISBN-13 do livro.
     */
    public void cadastrarLivroISBN(String isbn) {
        try {
            Livro livro = ISBNApiClient.buscarLivroPorISBN(isbn); // Busca o livro via API
            if (livro != null) {
                abrirLivroDialog(livro); // Abre o diálogo de edição com os dados do livro
            } else {
                JOptionPane.showMessageDialog(this, "Livro não encontrado na API.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar livro pelo ISBN: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Erro ao buscar livro pelo ISBN", ex);
        }
    }

    /**
     * Atualiza a tabela com os livros cadastrados.
     *
     * @param e Evento de ação disparado pelo botão atualizar.
     */
    private void atualizarTabela(ActionEvent e) {
        carregarLivros(); // Recarrega os livros
    }
}
