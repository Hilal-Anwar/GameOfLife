package org.game;

import org.game.conway_game_of_life.Game;
import org.game.conway_game_of_life.GameMode;
import org.game.conway_game_of_life.GridNature;
import org.game.util.Colors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String color = (args.length == 0) ? "" : args[0];
        String nature=(args.length > 0) ? args[1]:"";
        String mode=(args.length > 1) ? args[2]:"";
        if (color.isEmpty()) {
            var game = new Game(60, 30, 3);
            game.start();
        } else {
            try {
                var game = new Game(60, 30, 3, Colors.valueOf(color), GameMode.valueOf(mode),
                        GridNature.valueOf(nature));
                game.start();
            } catch (IllegalArgumentException e) {
                var game = new Game(60, 30, 3);
                game.start();
            }
        }
    }
}
