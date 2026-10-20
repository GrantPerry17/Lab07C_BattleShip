
public class Cell {

    private Ship ship;
    private boolean firedUpon;

    public Cell() {
        ship = null;
        firedUpon = false;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public boolean hasBeenFiredUpon() {
        return firedUpon;
    }

    public void setFiredUpon(boolean firedUpon) {
        this.firedUpon = firedUpon;
    }

    public boolean isHit() {
        return firedUpon && hasShip();
    }

    public boolean isMiss() {
        return firedUpon && !hasShip();
    }
}
