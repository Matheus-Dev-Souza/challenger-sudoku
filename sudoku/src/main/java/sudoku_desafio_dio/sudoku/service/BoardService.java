package sudoku_desafio_dio.sudoku.service;

import sudoku_desafio_dio.sudoku.model.Board;
import sudoku_desafio_dio.sudoku.model.GameStatusEnum;
import sudoku_desafio_dio.sudoku.model.Space;

import java.util.*;
import java.util.stream.IntStream;

public class BoardService {

    private static final int BOARD_LIMIT = 9;
    private final Board board;

    public BoardService(final Map<String, String> gameConfig) {
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces() {
        return board.getSpaces();
    }

    public void reset() {
        board.reset();
    }

    public boolean hasErrors() {
        return board.hasErrors();
    }

    public GameStatusEnum getStatus() {
        return board.getStatus();
    }

    public boolean gameIsFinished() {
        return board.gameIsFinished();
    }

    // NOVO: Método para gerar sugestões de possíveis valores válidos
    public Set<Integer> getSuggestions(int row, int col) {
        Space space = board.getSpaces().get(row).get(col);
        if (space.isFixed() || space.getActual() != null) {
            return Set.of(); // Nenhuma sugestão se a célula está preenchida ou é fixa
        }

        Set<Integer> possible = new HashSet<>(IntStream.rangeClosed(1, 9).boxed().toList());

        // Remove da linha
        for (int i = 0; i < 9; i++) {
            Integer val = board.getSpaces().get(row).get(i).getActual();
            if (val != null) possible.remove(val);
        }

        // Remove da coluna
        for (int i = 0; i < 9; i++) {
            Integer val = board.getSpaces().get(i).get(col).getActual();
            if (val != null) possible.remove(val);
        }

        // Remove do bloco 3x3
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                Integer val = board.getSpaces().get(i).get(j).getActual();
                if (val != null) possible.remove(val);
            }
        }

        return possible;
    }

    private List<List<Space>> initBoard(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++) {
                String key = "%s,%s".formatted(i, j);
                String positionConfig = gameConfig.get(key);

                if (positionConfig == null || !positionConfig.contains(",")) {
                    throw new IllegalArgumentException("Configuração inválida para a posição: " + key);
                }

                String[] parts = positionConfig.split(",");
                int expected = Integer.parseInt(parts[0]);
                boolean fixed = Boolean.parseBoolean(parts[1]);
                row.add(new Space(expected, fixed));
            }
            spaces.add(row);
        }

        return spaces;
    }
}
