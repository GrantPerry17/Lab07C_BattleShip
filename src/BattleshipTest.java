import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit 4 tests for the Battleship assignment.
 * Place this file in the same source folder/package as the Battleship classes.
 */
public class BattleshipTest {

    @Test
    public void shipStartsWithNoHitsAndSinksAtItsLength() {
        Ship ship = new Ship(3);

        assertEquals(3, ship.getLength());
        assertEquals(0, ship.getHits());
        assertFalse(ship.isSunk());

        ship.registerHit();
        ship.registerHit();
        assertEquals(2, ship.getHits());
        assertFalse(ship.isSunk());

        ship.registerHit();
        assertEquals(3, ship.getHits());
        assertTrue(ship.isSunk());

        // A sunk ship should not accumulate additional hits.
        ship.registerHit();
        assertEquals(3, ship.getHits());
    }

    @Test
    public void shipRejectsNonPositiveLength() {
        try {
            new Ship(0);
            fail("Expected IllegalArgumentException for a zero-length ship.");
        } catch (IllegalArgumentException expected) {
            // Expected behavior.
        }

        try {
            new Ship(-1);
            fail("Expected IllegalArgumentException for a negative-length ship.");
        } catch (IllegalArgumentException expected) {
            // Expected behavior.
        }
    }

    @Test
    public void cellTracksShipAndShotResult() {
        Cell cell = new Cell();

        assertFalse(cell.hasShip());
        assertFalse(cell.hasBeenFiredUpon());
        assertFalse(cell.isHit());
        assertFalse(cell.isMiss());

        Ship ship = new Ship(2);
        cell.setShip(ship);
        assertTrue(cell.hasShip());
        assertSame(ship, cell.getShip());

        cell.setFiredUpon(true);
        assertTrue(cell.hasBeenFiredUpon());
        assertTrue(cell.isHit());
        assertFalse(cell.isMiss());
    }

    @Test
    public void emptyCellIsAMissWhenFiredUpon() {
        Cell cell = new Cell();

        cell.setFiredUpon(true);

        assertTrue(cell.hasBeenFiredUpon());
        assertFalse(cell.isHit());
        assertTrue(cell.isMiss());
    }

    @Test
    public void boardHasTenByTenCellsAndSeventeenShipSpaces() {
        Board board = new Board();
        int occupiedCells = 0;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.getCell(row, col);
                assertNotNull(cell);
                if (cell.hasShip()) {
                    occupiedCells++;
                }
            }
        }

        assertEquals(17, occupiedCells);
    }
}