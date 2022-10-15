# Conway's Game of Life


![Untitled video - Made with Clipchamp](https://user-images.githubusercontent.com/50636048/196001237-27a4659d-cdaf-4369-b71c-f5775a5cf021.gif)

The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970. It is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves. It is Turing complete and can simulate a universal constructor or any other Turing machine
# Rules
The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, live or dead (or populated and unpopulated, respectively). Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:

1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

# How does this game works ?
This game has a grid of size 100 x 50 with each box(cell) of size 3.
There is a yellow color cursur which can move throughout the grid using the arrow keys.You can either select or deselect a cell using the enter key for
drawing the initial pattern.
Use the space bar to play and pause the game.
There is no infinite number of cells in this game so any patter moving beyond the grid will come outside from the other side.




https://user-images.githubusercontent.com/50636048/193699960-a0fb98f9-f26e-4d0d-9ea5-c005c9d9409e.mp4



https://user-images.githubusercontent.com/50636048/193699975-f80ee544-5c16-4429-bdbb-fc00a3044be0.mp4



https://user-images.githubusercontent.com/50636048/193699991-dd53bbc4-0f53-456f-9a49-95705cf27274.mp4



https://user-images.githubusercontent.com/50636048/193700028-3d5d19ad-9943-4fc6-95d5-6a77132b98e0.mp4

## How to download the source code ?


```
git clone https://github.com/Hilal-Anwar/GameOfLife.git

or

gh repo clone Hilal-Anwar/GameOfLife
```

## How to build it?
```
### Requirement
Java 19 or above
Maven

### Build

mvn clean compile assembly:single

```

## How to run it ?

```
cd target
java -jar GameOfLife-1.0-SNAPSHOT-jar-with-dependencies.jar RED GRID_FREE CLOSED


### Colors
RED
GREEN
YELLOW
BLUE
PURPLE
CYAN
WHITE

### Grid Type
GRID_FREE
GRID

### Type
CLOSED
OPEN
```
