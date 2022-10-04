package org.game.conway_game_of_life;


import org.game.util.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;


public class Game implements CheckMovement {
    private final int column;
    private final int row;
    private final int[] m_box = new int[]{3, 5};
    private final int size;
    private final boolean[][] dummy_grid;
    private final HashSet<Cell> memory = new HashSet<>();
    private final HashSet<Cell> dead_in_next_gen = new HashSet<>();
    private final HashSet<Cell> new_birth_in_next_gen = new HashSet<>();
    private String _color;

    public Game(int column, int row, int size, Colors color) {
        this.column = column;
        this.row = row;
        this.size = size;
        _color = Text.getColorText("██", color);
        dummy_grid = new boolean[row][column];
    }

    public Game(int column, int row, int size) {
        this.column = column;
        this.row = row;
        this.size = size;
        _color = Text.getColorText("██", Colors.values()[new Random().nextInt(8)]);
        dummy_grid = new boolean[row][column];
    }

    public void start() throws InterruptedException {
        var display = new Display();
        KeyBoardInput keyBoardInput = new KeyBoardInput(display);
        display.clear_display();
        Draw_Grid_box();
        while (true) {
            var key = keyBoardInput.getKeyBoardKey();
            switch (key) {
                case UP -> m_box[1] = m_box[1] != 0 ? m_box[1] - 1 : row - 1;
                case DOWN -> m_box[1] = m_box[1] != row - 1 ? m_box[1] + 1 : 0;
                case RIGHT -> m_box[0] = m_box[0] != column - 1 ? m_box[0] + 1 : 0;
                case LEFT -> m_box[0] = m_box[0] != 0 ? m_box[0] - 1 : column - 1;
                case ESC -> System.exit(-1);
                case ENTER -> {
                    dummy_grid[m_box[1]][m_box[0]] = !dummy_grid[m_box[1]][m_box[0]];
                    var k = new Cell(m_box[0], m_box[1]);
                    if (!memory.contains(k))
                        memory.add(k);
                    else memory.remove(k);
                }
            }
            if (!key.equals(Key.NONE)) {
                display.clear_display();
                Draw_Grid_box();
            }
            if (key.equals(Key.SPACE)) {
                create_next_gen();
                break;
            }
            keyBoardInput.setKeyBoardKey(Key.NONE);
            Thread.sleep(10);

        }
        display.clear_display();
        String condition = "paused";
        while (!memory.isEmpty() || !new_birth_in_next_gen.isEmpty()) {
            Draw_Grid();
            var key = keyBoardInput.getKeyBoardKey();
            switch (key) {
                case ESC -> System.exit(-1);
                case SPACE -> {
                    condition = condition.equals("play") ? "paused" : "play";
                    keyBoardInput.setKeyBoardKey(Key.NONE);
                }
            }
            if (condition.equals("play")) {
                create_next_gen();
            }

            Thread.sleep(90);
            display.clear_display();
        }
        System.exit(-1);
    }

    private void create_next_gen() {
        for (var m : memory) {
            var neighbour = count_for_neighbour(m.x(), m.y());
            int a = neighbour.living_neighbour_cells().size();
            if (a != 3 && a != 2) {
                dead_in_next_gen.add(m);
            }
            var dead = neighbour.dead_neighbour_cells();
            for (var t : dead) {
                var n = count_for_neighbour(t.x(), t.y());
                if (n.living_neighbour_cells().size() == 3)
                    new_birth_in_next_gen.add(t);
            }
        }
        for (var z : new_birth_in_next_gen) {
            dummy_grid[z.y()][z.x()] = true;
            memory.add(z);
        }
        for (var k : dead_in_next_gen) {
            dummy_grid[k.y()][k.x()] = false;
            memory.remove(k);
        }
        new_birth_in_next_gen.clear();
        dead_in_next_gen.clear();
    }

    private NeighbourHood count_for_neighbour(int x, int y) {
        HashSet<Cell> dead_neighbour_cells = new HashSet<>();
        HashSet<Cell> living_neighbour_cells = new HashSet<>();
        var list = CheckMovement.class.getMethods();
        for (var function : list) {
            try {
                var result = function.invoke(this, x, y);
                if (result instanceof Cell c) {
                    if (dummy_grid[c.y()][c.x()]) {
                        living_neighbour_cells.add(c);
                    } else {
                        dead_neighbour_cells.add(c);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return new NeighbourHood(dead_neighbour_cells, living_neighbour_cells);
    }


    private void Draw_Grid_box() {
        String color_text = Text.getColorText("██", Colors.YELLOW);
        StringBuilder grid = new StringBuilder();
        for (int i = 0; i < row * size + 1; i++) {
            for (int j = 0; j < column * size + 1; j++)
                if ((i % size == 0) || (j % size == 0) || (dummy_grid[i / size][j / size])) {
                    if (((j == m_box[0] * size || j == m_box[0] * size + size) &&
                            (i >= m_box[1] * size && i <= m_box[1] * size + size)))
                        grid.append(color_text);
                    else if (((i == m_box[1] * size || i == m_box[1] * size + size) &&
                            (j >= m_box[0] * size && j <= m_box[0] * size + size)))
                        grid.append(color_text);
                    else
                        grid.append("██");
                } else
                    grid.append("  ");
            grid.append('\n');
        }
        System.out.println(grid);
    }

    private void Draw_Grid() {
        StringBuilder grid = new StringBuilder();
        for (int i = 0; i < row * size; i++) {
            for (int j = 0; j < column * size; j++)
                grid.append(dummy_grid[i / (size)][j / (size)] ? _color: "  ");
            grid.append('\n');
        }
        System.out.println(grid);
    }

    @Override
    public Cell check_moveUp(int x, int y) {
        return new Cell(resolve_limit_of_x(x), resolve_limit_of_y(y - 1));
    }

    @Override
    public Cell check_moveDown(int x, int y) {
        return new Cell(resolve_limit_of_x(x), resolve_limit_of_y(y + 1));
    }

    @Override
    public Cell check_moveLeft(int x, int y) {
        return new Cell(resolve_limit_of_x(x - 1), resolve_limit_of_y(y));
    }

    @Override
    public Cell check_moveRight(int x, int y) {
        return new Cell(resolve_limit_of_x(x + 1), resolve_limit_of_y(y));
    }

    @Override
    public Cell check_moveDiagonal_upRight(int x, int y) {
        return new Cell(resolve_limit_of_x(x + 1), resolve_limit_of_y(y - 1));
    }

    @Override
    public Cell check_moveDiagonal_downRight(int x, int y) {
        return new Cell(resolve_limit_of_x(x + 1), resolve_limit_of_y(y + 1));
    }

    @Override
    public Cell check_moveDiagonal_upLeft(int x, int y) {
        return new Cell(resolve_limit_of_x(x - 1), resolve_limit_of_y(y - 1));
    }

    @Override
    public Cell check_moveDiagonal_downLeft(int x, int y) {
        return new Cell(resolve_limit_of_x(x - 1), resolve_limit_of_y(y + 1));
    }

    int resolve_limit_of_x(int x) {
        if (x < 0)
            return this.column - 1;
        if (x > this.column - 1)
            return 0;
        return x;
    }

    int resolve_limit_of_y(int y) {
        if (y < 0)
            return this.row - 1;
        if (y > this.row - 1)
            return 0;
        return y;
    }
}

