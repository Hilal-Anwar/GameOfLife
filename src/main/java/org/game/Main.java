package org.game;

import org.game.conway_game_of_life.Game;
import org.game.util.Colors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String color = (args.length == 0) ? "" : args[0];
        if (color.isEmpty()) {
            var game = new Game(60, 30, 3);
            game.start();
        } else {
            try {
                var game = new Game(60, 30, 3, Colors.valueOf(color));
                game.start();
            } catch (IllegalArgumentException e) {
                var game = new Game(60, 30, 3);
                game.start();
            }
        }
    }
}
