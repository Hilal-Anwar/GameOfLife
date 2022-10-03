package org.game.conway_game_of_life;

import java.util.HashSet;

record NeighbourHood(HashSet<Cell> dead_neighbour_cells, HashSet<Cell> living_neighbour_cells) {
}
