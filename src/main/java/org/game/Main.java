package org.game;

import org.game.conway_game_of_life.Game;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var game = new Game(60, 30, 3);
        game.start();
    }
}
