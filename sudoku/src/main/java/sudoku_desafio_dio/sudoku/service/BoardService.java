package sudoku_desafio_dio.sudoku.service;

import sudoku_desafio_dio.sudoku.model.Board;
import sudoku_desafio_dio.sudoku.model.GameStatusEnum;
import sudoku_desafio_dio.sudoku.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<List<Space>> initBoard(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++) {
                String key = "%s,%s".formatted(i, j);
                String positionConfig = gameConfig.get(key);

                if (positionConfig == null || !positionConfig.contains(",")) {
                    throw new IllegalArgumentException("Configuração inválida para a posição: " + key + 
                        ". Esperado formato: \"<valor>,<fixo>\", ex: \"5,true\"");
                }

                String[] parts = positionConfig.split(",");
                try {
                    int expected = Integer.parseInt(parts[0]);
                    boolean fixed = Boolean.parseBoolean(parts[1]);
                    row.add(new Space(expected, fixed));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Erro ao processar a posição " + key + 
                        ". Verifique se o valor é numérico e o booleano está correto (true/false).", ex);
                }
            }
            spaces.add(row);
        }

        return spaces;
    }
}
