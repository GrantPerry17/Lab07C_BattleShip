
public class Game {

    private Board board;

    private int missCounter;
    private int strikeCounter;
    private int totalMisses;
    private int totalHits;

    private boolean gameOver;

    public Game() {
        startNewGame();
    }

    public void startNewGame() {
        board = new Board();

        missCounter = 0;
        strikeCounter = 0;
        totalMisses = 0;
        totalHits = 0;

        gameOver = false;
    }

    public ShotResult fireShot(int row, int col) {
        if (gameOver) {
            return new ShotResult(
                false, true, false, false, false, 0
            );
        }

        Cell cell = board.getCell(row, col);

        if (cell.hasBeenFiredUpon()) {
            return new ShotResult(
                false, true, false, false, false, 0
            );
        }

        cell.setFiredUpon(true);

        Ship ship = cell.getShip();

        if (ship != null) {
            // HIT
            ship.registerHit();
            totalHits++;
            missCounter = 0;

            boolean sunk = ship.isSunk();
            boolean won = board.allShipsSunk();

            if (won) {
                gameOver = true;
            }

            return new ShotResult(
                true, false, sunk, won, false,
                ship.getLength()
            );

        } else {
            // MISS
            missCounter++;
            totalMisses++;

            if (missCounter >= 5) {
                strikeCounter++;
                missCounter = 0;
            }

            boolean lost = strikeCounter >= 3;

            if (lost) {
                gameOver = true;
            }

            return new ShotResult(
                false, false, false, false, lost, 0
            );
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getMissCounter() {
        return missCounter;
    }

    public int getStrikeCounter() {
        return strikeCounter;
    }

    public int getTotalMisses() {
        return totalMisses;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static class ShotResult {

        public final boolean hit;
        public final boolean alreadyFired;
        public final boolean shipSunk;
        public final boolean won;
        public final boolean lost;
        public final int shipLength;

        public ShotResult(
                boolean hit,
                boolean alreadyFired,
                boolean shipSunk,
                boolean won,
                boolean lost,
                int shipLength) {

            this.hit = hit;
            this.alreadyFired = alreadyFired;
            this.shipSunk = shipSunk;
            this.won = won;
            this.lost = lost;
            this.shipLength = shipLength;
        }
    }
}
