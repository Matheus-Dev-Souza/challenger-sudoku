package sudoku_desafio_dio.sudoku.ui.custom.panel;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MainPanel(final Dimension dimension){
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setLayout(new GridLayout(3, 3, 5, 5)); // 3x3 grid para os setores
    }
}
