package org.game.conway_game_of_life;

interface CheckMovement {
     Cell check_moveUp(int x, int y);
     Cell check_moveDown(int x, int y);
     Cell check_moveLeft(int x, int y);
     Cell check_moveRight(int x, int y);
     Cell check_moveDiagonal_upRight(int x, int y);
     Cell check_moveDiagonal_downRight(int x, int y);
     Cell check_moveDiagonal_upLeft(int x, int y);
     Cell check_moveDiagonal_downLeft(int x, int y);
}

