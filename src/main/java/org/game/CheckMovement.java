package org.game;

public interface CheckMovement {
     int[] check_moveUp(int x,int y);
     int[] check_moveDown(int x,int y);
     int[] check_moveLeft(int x,int y);
     int[] check_moveRight(int x,int y);
     int[] check_moveDiagonal_upRight(int x,int y);
     int[] check_moveDiagonal_downRight(int x,int y);
     int[] check_moveDiagonal_upLeft(int x,int y);
     int[] check_moveDiagonal_downLeft(int x,int y);
}

