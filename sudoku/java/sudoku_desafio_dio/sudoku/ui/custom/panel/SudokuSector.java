package sudoku_desafio_dio.sudoku.ui.custom.panel;

import sudoku_desafio_dio.sudoku.ui.custom.input.NumberText;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

public class SudokuSector extends JPanel {

    private static final long serialVersionUID = 1L;

    public SudokuSector(final List<NumberText> textFields){
        var dimension = new Dimension(170, 170);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setBorder(new LineBorder(Color.BLACK, 2, true));
        this.setLayout(new GridLayout(3, 3)); // 3x3 grid para as células
        textFields.forEach(this::add);
    }
}
