package giovanna.projeto.livraria1.view;

import giovanna.projeto.livraria1.model.Livro;
import giovanna.projeto.livraria1.services.LivroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;

/**
 * Classe responsável pela exibição de uma janela para selecionar livros similares.
 * Esta janela permite ao usuário escolher livros já cadastrados, exceto o próprio livro em edição.
 */
public class SelecionarLivroDialog extends JDialog {

    private final DefaultTableModel modeloTabelaLivros;  // Modelo da tabela de livros
    private final JTable tabelaLivros;                    // Tabela para exibição dos livros
    private final JButton btnOk;                          // Botão OK
    private final JButton btnCancelar;                    // Botão Cancelar
    private List<Livro> livrosDisponiveis;                // Lista de livros disponíveis para seleção
    private final List<Integer> livrosSelecionados = new ArrayList<>(); // Lista de livros selecionados

    private final int etiquetaLivroAtual;  // Etiqueta do livro atual (para evitar a seleção do próprio livro)
    private final LivroService livroService; // Serviço para interagir com a base de dados de livros

    /**
     * Construtor da classe SelecionarLivroDialog.
     * 
     * @param parent Janela pai que chamou o diálogo.
     * @param etiquetaLivroAtual Etiqueta do livro atual em edição, para não permitir sua seleção.
     */
    public SelecionarLivroDialog(Window parent, int etiquetaLivroAtual) {
        super(parent, "Selecionar Livros Similares", ModalityType.APPLICATION_MODAL);
        this.etiquetaLivroAtual = etiquetaLivroAtual;
        this.livroService = new LivroService();

        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Configuração do modelo da tabela
        modeloTabelaLivros = new DefaultTableModel(new String[]{"Selecionar", "Etiqueta", "Título", "Autor"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // A primeira coluna é uma checkbox
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna 0 (checkbox) pode ser editada
                return column == 0;
            }
        };

        tabelaLivros = new JTable(modeloTabelaLivros); // Criação da tabela
        JScrollPane scrollPane = new JScrollPane(tabelaLivros); // Adiciona scroll à tabela

        // Criação dos botões
        btnOk = new JButton("OK");
        btnCancelar = new JButton("Cancelar");

        // Layout dos botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancelar);

        // Adiciona os componentes ao painel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Define as ações dos botões
        btnOk.addActionListener(e -> selecionarLivros());  // Selecionar livros
        btnCancelar.addActionListener(e -> dispose());    // Fechar diálogo

        // Carrega os livros disponíveis para seleção
        carregarLivros();

        setVisible(true);
    }

    /**
     * Carrega a lista de livros disponíveis para seleção, excluindo o livro atual.
     * Preenche a tabela com os livros para que o usuário possa selecioná-los.
     */
    private void carregarLivros() {
        try {
            // Obtém a lista de livros
            livrosDisponiveis = livroService.lista_livros();

            modeloTabelaLivros.setRowCount(0); // Limpa a tabela antes de adicionar novos dados

            // Adiciona os livros à tabela, excluindo o livro atual
            for (Livro livro : livrosDisponiveis) {
                if (livro.getEtiqueta_livro() != etiquetaLivroAtual) { // Evita o livro atual
                    modeloTabelaLivros.addRow(new Object[]{false, livro.getEtiqueta_livro(), livro.getTitulo(), livro.getAutor()});
                }
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + ex.getMessage());
        }
    }

    /**
     * Processa a seleção dos livros na tabela.
     * Os livros selecionados (checkbox marcado) são adicionados à lista de livrosSelecionados.
     */
    private void selecionarLivros() {
        livrosSelecionados.clear(); // Limpa a lista de livros selecionados

        // Percorre as linhas da tabela para verificar quais checkboxes foram marcados
        for (int i = 0; i < modeloTabelaLivros.getRowCount(); i++) {
            boolean selecionado = (boolean) modeloTabelaLivros.getValueAt(i, 0); // Obtém o estado da checkbox
            if (selecionado) {
                int etiqueta = (int) modeloTabelaLivros.getValueAt(i, 1); // Obtém a etiqueta do livro
                livrosSelecionados.add(etiqueta); // Adiciona à lista de livros selecionados
            }
        }

        // Fecha o diálogo após a seleção
        dispose();
    }

    /**
     * Retorna a lista de livros selecionados.
     * 
     * @return Lista de etiquetas dos livros selecionados.
     */
    public List<Integer> getLivrosSelecionados() {
        return livrosSelecionados;
    }
}
