
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {

    public static final int SIZE = 10;

    private final Cell[][] cells;
    private final Ship[] ships;
    private final Random random;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        ships = new Ship[] {
            new Ship(5),
            new Ship(4),
            new Ship(3),
            new Ship(3),
            new Ship(2)
        };
        random = new Random();

        initializeCells();
        placeShips();
    }

    private void initializeCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    private void placeShips() {
        for (Ship ship : ships) {
            List<Placement> placements = new ArrayList<>();

            // Find every possible horizontal placement.
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0;
                     col <= SIZE - ship.getLength(); col++) {
                    placements.add(
                        new Placement(row, col, true)
                    );
                }
            }

            // Find every possible vertical placement.
            for (int row = 0;
                 row <= SIZE - ship.getLength(); row++) {
                for (int col = 0; col < SIZE; col++) {
                    placements.add(
                        new Placement(row, col, false)
                    );
                }
            }

            // Randomize the order of possible placements.
            Collections.shuffle(placements, random);

            for (Placement placement : placements) {
                if (canPlaceShip(ship, placement)) {
                    putShipOnBoard(ship, placement);
                    break;
                }
            }
        }
    }

    private boolean canPlaceShip(
            Ship ship, Placement placement) {

        for (int i = 0; i < ship.getLength(); i++) {
            int row = placement.row;
            int col = placement.col;

            if (placement.horizontal) {
                col += i;
            } else {
                row += i;
            }

            if (cells[row][col].hasShip()) {
                return false;
            }
        }

        return true;
    }

    private void putShipOnBoard(
            Ship ship, Placement placement) {

        for (int i = 0; i < ship.getLength(); i++) {
            int row = placement.row;
            int col = placement.col;

            if (placement.horizontal) {
                col += i;
            } else {
                row += i;
            }

            cells[row][col].setShip(ship);
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= SIZE ||
            col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException(
                "Invalid board position."
            );
        }

        return cells[row][col];
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }

        return true;
    }

    private static class Placement {
        private final int row;
        private final int col;
        private final boolean horizontal;

        private Placement(
                int row, int col, boolean horizontal) {
            this.row = row;
            this.col = col;
            this.horizontal = horizontal;
        }
    }
}
