package giovanna.projeto.livraria1.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Classe que contém a lógica utilizada para autocompletar campos de texto.
 * Permite a associação de listas de itens com um JTextField e exibe sugestões de preenchimento em um menu suspenso.
 * Mantendo como classe para que a lógica possa ser reutilizada se necessário.
 * @param <O> Tipo genérico dos itens utilizados pra autocompletar.
 * @author Giovanna
 */
public class AutoComplete<O> {

    /**
     * Metódo que configura a função de autocompletar para um JTextField.
     * Basicamente, ao digitar no campo de texto um menu suspenso será exibido para o usuário com base no texto digitado.
     * @param <O> Tipo dos itens utilizados para gerar sugestões.
     * @param textField Campo de texto que será autocompletado.
     * @param popupMenu menu suspenso em que será exibido as sugestões.
     * @param items Lista de itens que serão utilizados como sugestões.
     * @param toStringFunction Função para converter os itens da lista para Strings.
     */
    public static <O> void configureAutoComplete(JTextField textField, JPopupMenu popupMenu, List<O> items, java.util.function.Function<O, String> toStringFunction) {
        // Adiciona um listener que detecta as mudanças no JTextField.
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterAndShowPopup(textField, popupMenu, items, toStringFunction);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterAndShowPopup(textField, popupMenu, items, toStringFunction);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            // não foi necessário neste caso.    
            }
        });
        //Adiciona um listener para fechar o menu popup quando o usuário clicar fora dele ou do campo de texto.
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closePopupOnClickOutside(textField, popupMenu, e);
            }
        });
    }
    /**
     * Fecha o menu suspenso quando o usuário clica fora menu ou do campo de texto.
     * @param textField campo de textoassociado ao menu suspenso.
     * @param popupMenu Menu suspenso que ecibe as sugestões de preenchimento.
     * @param e Evento de clique.
     */
    private static void closePopupOnClickOutside(JTextField textField, JPopupMenu popupMenu, MouseEvent e) {
        // Verifica se o popup está visivel.
        if (!popupMenu.isShowing()) {
            return;
        }
        Point p = e.getPoint();
        SwingUtilities.convertPointFromScreen(p, (Component) e.getSource());
        // Fecha o popup ao clicar fora do menu.
        if (!popupMenu.contains(p)) {
            popupMenu.setVisible(false);
        }
    }
    /**
     * Filtra a lista de itens com base no texto inserido no JTextField e então exibe sugestões.
     * @param <O> Tipo dos itens utiizados na geração das sugestões.
     * @param textField Campo de texto associado ao menu suspenso.
     * @param popupMenu Menu suspenso que exibe as sugestões de preenchimento.
     * @param items Lista de itens que poderão ser selecionados para autocompletar.
     * @param toStringFunction  Função que converte os itens para Strings.
     */
    private static <O> void filterAndShowPopup(JTextField textField, JPopupMenu popupMenu, List<O> items, java.util.function.Function<O, String> toStringFunction) {
        String text = textField.getText();
        popupMenu.removeAll(); //LImpa o menu primeiro.
        // Se o texto ou a lista estiverem vazias, o popup é ocultado.
        if (text.isEmpty() || items == null) {
            popupMenu.setVisible(false);
            return;
        }
        // Filtra os itens que contêm o texto digitado, não é case sensitive.
        List<String> filteredStrings = items.stream()
                .map(toStringFunction)
                .filter(s -> s.toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        if (filteredStrings.isEmpty()) {
            popupMenu.setVisible(false);
            return;
        }
        // Adiciona os itens filtrados ao menu suspenso.
        for (String filteredString : filteredStrings) {
            JMenuItem menuItem = new JMenuItem(filteredString);
            menuItem.addActionListener(e -> {
                textField.setText(filteredString);
                popupMenu.setVisible(false);
            });
            popupMenu.add(menuItem);
        }
        //Exibe o menu suspenso embaixo do JTextField.
        popupMenu.show(textField, 0, textField.getHeight());
    }
}