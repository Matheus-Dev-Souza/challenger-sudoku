package sudoku_desafio_dio.sudoku;

import sudoku_desafio_dio.sudoku.ui.custom.screen.MainScreen;

import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class UIMain {

    public static void main(String[] args) {
        final Map<String, String> gameConfig = new HashMap<>();

        
        if (args.length > 0) {
            Map<String, String> inputConfig = Stream.of(args)
                .map(s -> s.split(";"))
                .filter(parts -> parts.length == 2 && parts[0].contains(","))
                .collect(toMap(parts -> parts[0], parts -> parts[1]));

            gameConfig.putAll(inputConfig);
        }

        // Preenche o restante do tabuleiro com valores padrão (0,false)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String key = i + "," + j;
                gameConfig.putIfAbsent(key, "0,false"); // campo vazio e não fixo
            }
        }

        var mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }
}
