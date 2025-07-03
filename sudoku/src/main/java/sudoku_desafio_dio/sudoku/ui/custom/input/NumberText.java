package sudoku_desafio_dio.sudoku.ui.custom.input;

import sudoku_desafio_dio.sudoku.model.Space;
import sudoku_desafio_dio.sudoku.service.EventEnum;
import sudoku_desafio_dio.sudoku.service.EventListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static java.awt.Font.PLAIN;
import static sudoku_desafio_dio.sudoku.service.EventEnum.CLEAR_SPACE;

public class NumberText extends JTextField implements EventListener {

    private static final long serialVersionUID = 1L;
    private final Space space;
    private boolean draftMode = false;
    private final Set<Integer> sketches = new HashSet<>();

    public NumberText(final Space space) {
        this.space = space;

        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", PLAIN, 20));
        this.setHorizontalAlignment(CENTER);

        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new NumberTextLimit());
        this.setDocument(doc);

        this.setEnabled(!space.isFixed());

        if (space.isFixed()) {
            this.setText(space.getActual().toString());
        }

        // Document listener para modo normal
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                if (!draftMode) changeSpace();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                if (!draftMode) changeSpace();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                if (!draftMode) changeSpace();
            }

            private void changeSpace() {
                if (getText().isEmpty()) {
                    space.clearSpace();
                    return;
                }
                try {
                    space.setActual(Integer.parseInt(getText()));
                    space.clearDraftValues();
                    sketches.clear();
                } catch (NumberFormatException ex) {
                    // Ignora entrada inválida
                }
            }
        });

        // Key listener para modo rascunho com CTRL
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!space.isFixed() && e.isControlDown()) {
                    try {
                        int value = Integer.parseInt(getText());
                        if (sketches.contains(value)) {
                            sketches.remove(value);
                        } else {
                            sketches.add(value);
                        }
                        draftMode = true;
                        setText("");
                        repaint();
                    } catch (NumberFormatException ex) {
                        // ignora
                    }
                } else {
                    draftMode = false;
                    setFont(new Font("Arial", PLAIN, 20));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenhar rascunhos pequenos
        if (!sketches.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            int i = 0;
            for (Integer number : sketches) {
                int row = i / 3;
                int col = i % 3;
                g2.drawString(number.toString(), col * 15 + 5, row * 15 + 15);
                i++;
            }
        }
    }

    @Override
    public void update(final EventEnum eventType) {
        if (eventType.equals(CLEAR_SPACE) && this.isEnabled()) {
            this.setText("");
            space.clearSpace();
            sketches.clear();
            repaint();
        }
    }

    // Opcional: usado por outros componentes se quiser ativar modo rascunho programaticamente
    public void toggleSketchMode() {
        this.draftMode = !this.draftMode;
    }

    public void setSuggestions(Set<Integer> suggestions) {
        this.sketches.clear();
        this.sketches.addAll(suggestions);
        this.repaint();
    }
}
