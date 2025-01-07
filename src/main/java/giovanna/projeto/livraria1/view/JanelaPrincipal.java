package giovanna.projeto.livraria1.view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;

/**
 * Janela principal da aplicação, que contém o menu e um JTabbedPane para exibir
 * as telas como abas. Permite ao usuário navegar entre diferentes seções do
 * sistema.
 * 
 * Esta classe gerencia a interface principal do sistema, incluindo a criação de
 * menus e a navegação entre diferentes abas que exibem funcionalidades do sistema.
 * 
 * @author Giovanna
 */
public class JanelaPrincipal extends JFrame {

    private final JTabbedPane tabbedPane; // Contêiner de abas
    private final Map<String, JPanel> abasAbertas; // Controle das abas abertas

    /**
     * Construtor da JanelaPrincipal. Inicializa os componentes principais da
     * janela, incluindo o JTabbedPane e o menu.
     */
    public JanelaPrincipal() {
        setTitle("Sistema Livraria");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializa o JTabbedPane
        tabbedPane = new JTabbedPane();
        abasAbertas = new HashMap<>();

        // Adiciona o JTabbedPane à JanelaPrincipal
        add(tabbedPane, BorderLayout.CENTER);

        // Configura o menu
        setJMenuBar(criarMenu());
    }

    /**
     * Cria o menu da janela principal.
     *
     * @return A barra de menus configurada para a JanelaPrincipal.
     */
    private JMenuBar criarMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Cadastro
        JMenu menuCadastro = new JMenu("Cadastro");
        adicionarItemMenu(menuCadastro, "Livros", e -> {
            try {
                abrirAba("Cadastro de livros", new TelaCadastroLivros());
            } catch (ServiceException | SQLException ex) {
                Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        adicionarItemMenu(menuCadastro, "Gêneros", e -> {
            try {
                abrirAba("Cadastro de gêneros", new TelaCadastroGenero());
            } catch (SQLException ex) {
                Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Menu Relatório
        JMenu menuRelatorio = new JMenu("Relatórios");
        adicionarItemMenu(menuRelatorio, "Relatório por Gênero", e -> abrirAba("Relatório por Gênero", new TelaRelatorioGenero()));

        // Menu Catálogo
        JMenu menuCatalogo = new JMenu("Catálogo");
        adicionarItemMenu(menuCatalogo, "Livro", e -> {
            try {
                abrirAba("Catálogo de livros", new TelaFiltrarLivros());
            } catch (SQLException ex) {
                Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem menuSair = new JMenuItem("Sair");
        menuSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(menuSair);

        // Adiciona menus à barra de menu
        menuBar.add(menuCadastro);
        menuBar.add(menuCatalogo);
        menuBar.add(menuRelatorio);
        menuBar.add(menuArquivo);
        return menuBar;
    }

    /**
     * Adiciona um item de menu ao menu.
     *
     * @param menu O menu ao qual o item será adicionado.
     * @param nome O nome do item.
     * @param acao A ação a ser executada quando o item for clicado.
     */
    private void adicionarItemMenu(JMenu menu, String nome, java.awt.event.ActionListener acao) {
        JMenuItem item = new JMenuItem(nome);
        item.addActionListener(acao);
        menu.add(item);
    }

    /**
     * Abre uma nova aba ou foca na aba existente.
     *
     * @param titulo Título da aba.
     * @param painel Painel a ser exibido na aba.
     */
    private void abrirAba(String titulo, JPanel painel) {
        // Verifica se a aba já está aberta
        if (abasAbertas.containsKey(titulo)) {
            tabbedPane.setSelectedComponent(abasAbertas.get(titulo)); // Foca na aba existente
            return;
        }

        // Adiciona o painel ao JTabbedPane
        tabbedPane.addTab(titulo, painel);
        abasAbertas.put(titulo, painel);

        // Monta o cabeçalho com botão de fechar
        JPanel painelTituloComFechar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painelTituloComFechar.setOpaque(false); // Fundo transparente

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        painelTituloComFechar.add(lblTitulo);

        // Botão de fechar
        JButton btnFechar = criarBotaoFechar(titulo, painel);
        painelTituloComFechar.add(btnFechar);

        // Define o cabeçalho personalizado da aba
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(painel), painelTituloComFechar);

        // Foca na aba recém-adicionada
        tabbedPane.setSelectedComponent(painel);
    }

    /**
     * Cria um botão de fechar estilizado para as abas.
     *
     * @param titulo Título da aba.
     * @param painel Painel associado à aba.
     * @return O botão de fechar configurado.
     */
    private JButton criarBotaoFechar(String titulo, JPanel painel) {
        JButton btnFechar = new JButton("X");

        // Configurações para ajustar o botão
        btnFechar.setFont(new Font("Arial", Font.PLAIN, 10 )); // Fonte 
        btnFechar.setPreferredSize(new Dimension(16, 16)); // Tamanho 
        btnFechar.setMargin(new Insets(0, 0, 0, 0)); // Remove margens internas
        btnFechar.setFocusPainted(false); // Remove o foco visual
        btnFechar.setContentAreaFilled(false); // Remove o fundo padrão
        btnFechar.setBorderPainted(false); // Remove a borda padrão
        btnFechar.setForeground(Color.RED); // Define a cor do texto como vermelho
        btnFechar.setToolTipText("Fechar aba"); // Tooltip para o botão

        // Ação do botão de fechar
        btnFechar.addActionListener(e -> {
            int index = tabbedPane.indexOfComponent(painel);
            if (index != -1) {
                tabbedPane.remove(index); // Remove a aba
                abasAbertas.remove(titulo); // Remove do controle de abas abertas
            }
        });

        return btnFechar;
    }

    /**
     * Método para exibir uma mensagem de erro para o usuário em caso de falha.
     *
     * @param mensagem A mensagem de erro a ser exibida.
     */
    private void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
