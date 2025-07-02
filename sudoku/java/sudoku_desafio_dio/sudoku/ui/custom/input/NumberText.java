package sudoku_desafio_dio.sudoku.ui.custom.input;

import sudoku_desafio_dio.sudoku.model.Space;
import sudoku_desafio_dio.sudoku.service.EventEnum;
import sudoku_desafio_dio.sudoku.service.EventListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;


import java.awt.Dimension;
import java.awt.Font;

import static sudoku_desafio_dio.sudoku.service.EventEnum.CLEAR_SPACE;
import static java.awt.Font.PLAIN;

public class NumberText extends JTextField implements EventListener {
    
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
    private final Space space;

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

        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(final DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                changeSpace();
            }

            private void changeSpace() {
                if (getText().isEmpty()) {
                    space.clearSpace();
                    return;
                }
                try {
                    space.setActual(Integer.parseInt(getText()));
                } catch (NumberFormatException ex) {
                    // Ignora entrada inválida
                }
            }
        });
    }

    @Override
    public void update(final EventEnum eventType) {
        if (eventType.equals(CLEAR_SPACE) && this.isEnabled()) {
            this.setText("");
        }
    }
}
