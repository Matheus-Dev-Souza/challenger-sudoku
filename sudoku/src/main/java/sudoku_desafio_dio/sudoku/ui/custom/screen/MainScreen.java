package sudoku_desafio_dio.sudoku.ui.custom.screen;

import sudoku_desafio_dio.sudoku.model.Space;
import sudoku_desafio_dio.sudoku.service.BoardService;
import sudoku_desafio_dio.sudoku.service.NotifierService;
import sudoku_desafio_dio.sudoku.ui.custom.button.CheckGameStatusButton;
import sudoku_desafio_dio.sudoku.ui.custom.button.FinishGameButton;
import sudoku_desafio_dio.sudoku.ui.custom.button.ResetButton;
import sudoku_desafio_dio.sudoku.ui.custom.frame.MainFrame;
import sudoku_desafio_dio.sudoku.ui.custom.input.NumberText;
import sudoku_desafio_dio.sudoku.ui.custom.panel.MainPanel;
import sudoku_desafio_dio.sudoku.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sudoku_desafio_dio.sudoku.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.*;

public class MainScreen {

    private static final Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;

    private List<NumberText> numberFields;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
        this.numberFields = new ArrayList<>();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);

        for (int r = 0; r < 9; r += 3) {
            int endRow = r + 2;
            for (int c = 0; c < 9; c += 3) {
                int endCol = c + 2;
                List<Space> spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }

        addControlButtons(mainPanel);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(List<List<Space>> spaces, int initCol, int endCol, int initRow, int endRow) {
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(r).get(c));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(List<Space> spaces) {
        List<NumberText> fields = spaces.stream().map(NumberText::new).toList();
        numberFields.addAll(fields); // Guardamos os campos para sugestões
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel panel) {
        finishGameButton = new FinishGameButton(_ -> {
            if (boardService.gameIsFinished()) {
                showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                showMessageDialog(null, "Seu jogo tem alguma inconsistência, ajuste e tente novamente");
            }
        });
        panel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel panel) {
        checkGameStatusButton = new CheckGameStatusButton(_ -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            showMessageDialog(null, message);

            // Aqui geramos sugestões automáticas:
            generateSuggestions();
        });
        panel.add(checkGameStatusButton);
    }

    private void generateSuggestions() {
        for (NumberText field : numberFields) {
            if (!field.getSpace().isFixed() && field.getSpace().getActual() == null) {
                List<Integer> possibleValues = boardService.getAvailableValues(field.getSpace());
                field.showSketchSuggestions(possibleValues);
            }
        }
    }

    private void addResetButton(JPanel panel) {
        resetButton = new ResetButton(_ -> {
            int dialogResult = showConfirmDialog(
                null,
                "Deseja realmente reiniciar o jogo?",
                "Limpar o jogo",
                YES_NO_OPTION,
                QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        panel.add(resetButton);
    }

    private void addControlButtons(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addResetButton(buttonPanel);
        addCheckGameStatusButton(buttonPanel);
        addFinishGameButton(buttonPanel);
        mainPanel.add(buttonPanel);
    }
}
