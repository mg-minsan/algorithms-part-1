import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board {

    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        String toString = "";
        toString += tiles.length + "\n";

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                toString += " " + tiles[i][j];
            }
            toString += "\n";
        }
        return toString;
    }

    // board dimension n
    public int dimension() {
        return tiles.length + 1;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != (i * tiles[i].length) + j + 1 && tiles[i][j] != 0) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                int currnetIndex = (i * tiles[i].length) + j + 1;
                if (tiles[i][j] != currnetIndex) {
                    int missplaced = Math.abs(currnetIndex - tiles[i][j]);
                    manhattan += missplaced / tiles.length + missplaced % tiles.length;
                }
            }
        }

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {

                if (i == tiles.length - 1 && j == tiles.length - 1) {
                    continue;
                }

                int currnetIndex = (i * tiles[i].length) + j + 1;
                if (tiles[i][j] != currnetIndex) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 0) {
                    y = i;
                    x = j;
                }
            }
        }

        ArrayList<Board> neighbors = new ArrayList<Board>();

        if (x + 1 < tiles.length) {
            neighbors.add(neighborBoard(x, y, x + 1, y));
        }

        if (x - 1 >= 0) {
            neighbors.add(neighborBoard(x, y, x - 1, y));
        }

        if (y + 1 < tiles.length) {
            neighbors.add(neighborBoard(x, y, x, y + 1));
        }

        if (y - 1 >= 0) {
            neighbors.add(neighborBoard(x, y, x, y - 1));
        }

        return new NeighborIerable(neighbors);
    }

    private Board neighborBoard(int blankX, int blankY, int x, int y) {
        int[][] neighborsTiles = copyTiles();
        neighborsTiles[blankY][blankX] = tiles[y][x];
        neighborsTiles[y][x] = this.tiles[blankY][blankX];
        return new Board(neighborsTiles);
    }

    private int[][] copyTiles() {
        int[][] copyTiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                copyTiles[i][j] = tiles[i][j];
            }
        }

        return copyTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (this.tiles[i][j] != 0) {
                    if (j + 1 < tiles.length) {
                        if (tiles[i][j + 1] != 0) {
                            return neighborBoard(j, i, j + 1, i);
                        }
                    } else {
                        if (tiles[i + 1][0] != 0) {
                            return neighborBoard(j, i, 0, i + 1);
                        }
                    }
                }
            }
        }
        return null;
    }

    private class NeighborIerable implements Iterable<Board> {

        ArrayList<Board> neighbors;

        public NeighborIerable(ArrayList<Board> neighbors) {
            this.neighbors = neighbors;
        }

        @Override
        public Iterator<Board> iterator() {
            return new NeighborIterator();
        }

        public class NeighborIterator implements Iterator<Board> {
            int index = 0;

            @Override
            public boolean hasNext() {
                if (index < neighbors.size()) {
                    return true;
                }
                return false;
            }

            @Override
            public Board next() {
                if (index < neighbors.size()) {
                    Board board = neighbors.get(index);
                    index++;
                    return board;
                }

                return null;
            }
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                { 0, 1 },
                { 2, 3 },
        };

        // int[][] tiles2 = {
        // { 1, 2, 3, 4 },
        // { 5, 6, 7, 8 },
        // { 9, 10, 11, 12 },
        // { 13, 14, 15, 0 },
        // };

        Board board = new Board(tiles);
        System.out.println(board.twin().toString());
        // for (Board bo : board.neighbors()) {
        // System.out.println(bo.toString());
        // }
    }
}