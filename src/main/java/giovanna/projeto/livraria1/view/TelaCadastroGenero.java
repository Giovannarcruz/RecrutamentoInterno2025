package giovanna.projeto.livraria1.view;

import giovanna.projeto.livraria1.services.GeneroService;
import giovanna.projeto.livraria1.model.Genero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe responsável pela interface de cadastro, edição e exclusão de gêneros.
 * Esta classe foi adaptada para ser utilizada como um {@link JPanel}, proporcionando
 * a interação com os gêneros cadastrados no sistema.
 */
public class TelaCadastroGenero extends JPanel {

    private final DefaultTableModel modeloTabelaGenero; // Modelo da tabela para exibir os gêneros
    private final JTable tabelaGenero; // Tabela para exibição dos gêneros
    private final GeneroService generoService; // Serviço responsável pela manipulação dos gêneros

    /**
     * Construtor para inicializar o painel de cadastro de gêneros.
     * Configura a tabela de gêneros e os botões de ação (Incluir, Editar e Excluir).
     *
     * @throws SQLException caso ocorra um erro ao carregar os gêneros.
     */
    public TelaCadastroGenero() throws SQLException {
        this.generoService = new GeneroService();
        setLayout(new BorderLayout());

        // Configuração do modelo da tabela
        modeloTabelaGenero = new DefaultTableModel(new String[]{"ID", "Nome"}, 0);
        tabelaGenero = new JTable(modeloTabelaGenero);
        JScrollPane scrollPaneGenero = new JScrollPane(tabelaGenero);

        // Botões de ação
        JPanel painelBotoes = new JPanel();
        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        // Layout do painel
        add(scrollPaneGenero, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnIncluir.addActionListener(e -> incluiGenero());
        btnEditar.addActionListener(e -> editaGenero());
        btnExcluir.addActionListener(e -> excluiGenero());

        // Carrega os gêneros na tabela ao inicializar
        carregaGeneros();
    }

    /**
     * Carrega os gêneros cadastrados na tabela.
     * Este método limpa a tabela antes de recarregar os dados.
     *
     * @throws SQLException caso ocorra um erro ao carregar os gêneros.
     */
    private void carregaGeneros() throws SQLException {
        try {
            List<Genero> generos = generoService.listaGeneros(); // Obtém a lista de gêneros
            modeloTabelaGenero.setRowCount(0); // Limpa a tabela

            // Adiciona os gêneros à tabela
            for (Genero genero : generos) {
                modeloTabelaGenero.addRow(new Object[]{genero.getId(), genero.getNome()});
            }
        } catch (SQLException e) {
            // Exibe mensagem de erro caso não seja possível carregar os gêneros
            JOptionPane.showMessageDialog(this, "Não foi possível carregar os gêneros cadastrados!");
        }
    }

    /**
     * Adiciona um novo gênero ao sistema.
     * O nome do gênero é obtido através de um campo de entrada.
     */
    private void incluiGenero() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do gênero:");
        if (nome != null && !nome.trim().isEmpty()) {
            try {
                Genero genero = new Genero();
                genero.setNome(nome); // Define o nome do novo gênero
                generoService.salvaGenero(genero); // Salva o gênero no banco de dados
                carregaGeneros(); // Atualiza a tabela
            } catch (SQLException e) {
                // Exibe mensagem de erro caso não seja possível incluir o gênero
                JOptionPane.showMessageDialog(this, "Não foi possível incluir o gênero: " + e.getMessage());
            }
        }
    }

    /**
     * Edita o gênero selecionado na tabela.
     * O usuário é solicitado a digitar um novo nome para o gênero.
     */
    private void editaGenero() {
        int linhaSelecionada = tabelaGenero.getSelectedRow();
        if (linhaSelecionada == -1) {
            // Exibe mensagem caso nenhuma linha tenha sido selecionada
            JOptionPane.showMessageDialog(this, "Selecione um registro para editar.");
            return;
        }

        // Obtém o id e o nome atual do gênero selecionado
        int id = (int) modeloTabelaGenero.getValueAt(linhaSelecionada, 0);
        String nomeAtual = (String) modeloTabelaGenero.getValueAt(linhaSelecionada, 1);
        String nomeNovo = JOptionPane.showInputDialog(this, "Editando o gênero:", nomeAtual);

        if (nomeNovo != null && !nomeNovo.trim().isEmpty()) {
            try {
                // Atualiza o nome do gênero no banco de dados
                Genero genero = new Genero();
                genero.setId(id);
                genero.setNome(nomeNovo);
                generoService.atualizaGenero(genero);
                carregaGeneros(); // Atualiza a tabela
            } catch (SQLException e) {
                // Exibe mensagem de erro caso não seja possível atualizar o gênero
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar o gênero: " + e.getMessage());
            }
        }
    }

    /**
     * Exclui o gênero selecionado na tabela.
     * O usuário é solicitado a confirmar a exclusão.
     */
    private void excluiGenero() {
        int linhaSelecionada = tabelaGenero.getSelectedRow();
        if (linhaSelecionada == -1) {
            // Exibe mensagem caso nenhuma linha tenha sido selecionada
            JOptionPane.showMessageDialog(this, "Selecione um registro para excluir.");
            return;
        }

        // Obtém o id do gênero selecionado
        int id = (int) modeloTabelaGenero.getValueAt(linhaSelecionada, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este gênero?", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // Exclui o gênero do banco de dados
                generoService.excluiGenero(id);
                carregaGeneros(); // Atualiza a tabela
            } catch (SQLException e) {
                // Exibe mensagem de erro caso não seja possível excluir o gênero
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o gênero: " + e.getMessage());
            }
        }
    }
}
