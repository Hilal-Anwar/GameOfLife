package org.game;


import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Grid implements CheckMovement {
    private final int column;
    private final int row;
    private final int[] m_box = new int[]{3, 5};
    private final KeyBoardInput keyBoardInput = new KeyBoardInput();
    private int size;
    private boolean[][] dummy_grid;
    private final HashMap<String, int[]> memory = new HashMap<>();
    private final HashSet<String> dead_in_next_gen = new HashSet<>();
    private final ArrayList<int[]> new_birth_in_next_gen = new ArrayList<>();

    public Grid(int column, int row, int size) {
        this.column = column;
        this.row = row;
        this.size = size;
        dummy_grid = new boolean[row][column];
    }

    public Grid(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        var grid = new Grid(100, 40, 3);
        grid.start();
    }

    void cls() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    void start() throws InterruptedException, IOException {
        while (true) {
            var key = keyBoardInput.getKeyBoardKey();
            switch (key) {
                case UP -> m_box[0] = m_box[0] != 0 ? m_box[0] - 1 : row - 1;
                case DOWN -> m_box[0] = m_box[0] != row - 1 ? m_box[0] + 1 : 0;
                case RIGHT -> m_box[1] = m_box[1] != column - 1 ? m_box[1] + 1 : 0;
                case LEFT -> m_box[1] = m_box[1] != 0 ? m_box[1] - 1 : column - 1;
                case ESC -> System.exit(-1);
                case ENTER -> {
                    dummy_grid[m_box[0]][m_box[1]] = !dummy_grid[m_box[0]][m_box[1]];
                    String k = "(" + m_box[1] + "," + m_box[0] + ")";
                    if (!memory.containsKey(k))
                        memory.put(k, new int[]{m_box[1], m_box[0]});
                    else memory.remove(k);
                }
            }
            Draw_Grid();
            if (key.equals(Key.SPACE)) {
                create_next_gen();
                break;
            }
            keyBoardInput.setKeyBoardKey(Key.NONE);
            //Draw_Grid();
            Thread.sleep(5);
            // if (keyBoardInput.terminal!=null)
            keyBoardInput.terminal.puts(InfoCmp.Capability.clear_screen);
            //cls();

        }
        //System.out.println(memory);
        //System.out.println("new birth "+new_birth_in_next_gen);
        while (!memory.isEmpty() || !new_birth_in_next_gen.isEmpty()) {
            Draw_Grid();
            Thread.sleep(50);
            create_next_gen();
            keyBoardInput.terminal.puts(InfoCmp.Capability.clear_screen);
        }
        System.exit(-1);
    }

    private void create_next_gen() {
        new_birth_in_next_gen.clear();
        dead_in_next_gen.clear();
        //System.out.println("first gen");
        for (var m : memory.values()) {
            var neighbour = count_for_neighbour(m[0], m[1]);
            int a = neighbour.living_neighbour_cells().size();
            //System.out.println(a+"  "+neighbour.living_neighbour_cells);
            if (a != 3 && a != 2) {
                dead_in_next_gen.add("(" + m[0] + "," + m[1] + ")");
            } else {
                var dead = neighbour.dead_neighbour_cells();
               /* System.out.println("dead  "+ Arrays.toString(m));
                for(var w:dead){
                    System.out.print(Arrays.toString(w)+" , ");
                }*/
                for (var t : dead) {
                    // System.out.println("gt   "+Arrays.toString(t));
                    var n = count_for_neighbour(t[0], t[1]);
                    if (n.living_neighbour_cells.size() == 3)
                        new_birth_in_next_gen.add(t);
                    //System.out.println("yt");

                }
            }
        }
        //System.out.println(dead_in_next_gen);
        //System.out.println(memory);
        for (var k : dead_in_next_gen) {
            var r = memory.get(k);
            dummy_grid[r[1]][r[0]] = false;
            memory.remove(k);
        }
        for (var z : new_birth_in_next_gen) {
            //System.out.println(Arrays.toString(z));
            dummy_grid[z[1]][z[0]] = true;
            memory.put("(" + z[0] + "," + z[1] + ")", z);
        }
        //System.out.println("end");
    }

    private NeighbourHood count_for_neighbour(int x, int y) {
        ArrayList<int[]> dead_neighbour_cells = new ArrayList<>();
        ArrayList<int[]> living_neighbour_cells = new ArrayList<>();
        var list = CheckMovement.class.getMethods();
        for (var function : list) {
            try {
                var result = function.invoke(this, x, y);
                if (result instanceof int[] coordinate) {
                    if (dummy_grid[coordinate[1]][coordinate[0]]) {
                        living_neighbour_cells.add(coordinate);
                    } else {
                        dead_neighbour_cells.add(coordinate);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return new NeighbourHood(dead_neighbour_cells, living_neighbour_cells);
    }

    record NeighbourHood(ArrayList<int[]> dead_neighbour_cells, ArrayList<int[]> living_neighbour_cells) {
    }

    public void Draw_Grid() {
        StringBuilder grid = new StringBuilder();
        for (int i = 0; i < row * size + 1; i++) {
            for (int j = 0; j < column * size + 1; j++)
                if ((i % size == 0) || (j % size == 0) || (dummy_grid[i / size][j / size])) {
                    if (((j == m_box[1] * size || j == m_box[1] * size + size) &&
                            (i >= m_box[0] * size && i <= m_box[0] * size + size)))
                        grid.append("\033[0;33m██\33[0m");
                    else if (((i == m_box[0] * size || i == m_box[0] * size + size) &&
                            (j >= m_box[1] * size && j <= m_box[1] * size + size)))
                        grid.append("\033[0;33m██\33[0m");
                    else
                        grid.append("██");
                } else
                    grid.append("  ");
            grid.append('\n');
        }
        System.out.println(grid);
    }

    @Override
    public int[] check_moveUp(int x, int y) {
        if (y - 1 < 0)
            return new int[]{x, row - 1};
        return new int[]{x, y - 1};
    }

    @Override
    public int[] check_moveDown(int x, int y) {
        if (y + 1 > row - 1)
            return new int[]{x, 0};
        return new int[]{x, y + 1};
    }

    @Override
    public int[] check_moveLeft(int x, int y) {
        if (x - 1 < 0)
            return new int[]{column - 1, y};
        return new int[]{x - 1, y};
    }

    @Override
    public int[] check_moveRight(int x, int y) {
        if (x + 1 > column - 1)
            return new int[]{0, y};
        return new int[]{x + 1, y};
    }

    @Override
    public int[] check_moveDiagonal_upRight(int x, int y) {
        if (x + 1 > column - 1 && y - 1 < 0)
            return new int[]{0, row - 1};
        if (x + 1 > column - 1)
            return new int[]{0, row - 1 - y};
        if (y - 1 < 0)
            return new int[]{column - x - 1, row - 1};
        return new int[]{x + 1, y - 1};
    }

    @Override
    public int[] check_moveDiagonal_downRight(int x, int y) {
        if (x + 1 > column - 1 && y + 1 > row - 1)
            return new int[]{0, 0};
        if (x + 1 > column - 1)
            return new int[]{0, y};
        if (y + 1 > row - 1)
            return new int[]{column - x - 1, 0};
        return new int[]{x + 1, y + 1};
    }

    @Override
    public int[] check_moveDiagonal_upLeft(int x, int y) {
        if (x - 1 < 0 && y - 1 < 0)
            return new int[]{column - 1, row - 1};
        if (x - 1 < 0)
            return new int[]{column - 1, row - 1 - y};
        if (y - 1 < 0)
            return new int[]{column - x - 1, row - 1};
        return new int[]{x - 1, y - 1};
    }

    @Override
    public int[] check_moveDiagonal_downLeft(int x, int y) {
        if (x - 1 < 0 && y + 1 < row - 1)
            return new int[]{column - 1, 0};
        if (x - 1 < 0)
            return new int[]{column - 1, y};
        if (y + 1 > row - 1)
            return new int[]{column - x - 1, row - 1};
        return new int[]{x - 1, y + 1};
    }
}
