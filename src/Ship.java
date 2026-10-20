
public class Ship {

    private final int length;
    private int hits;

    public Ship(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(
                "Ship length must be positive."
            );
        }

        this.length = length;
        this.hits = 0;
    }

    public int getLength() {
        return length;
    }

    public int getHits() {
        return hits;
    }

    public void registerHit() {
        if (!isSunk()) {
            hits++;
        }
    }

    public boolean isSunk() {
        return hits >= length;
    }
}
