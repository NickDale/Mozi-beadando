package hu.nje.mozifxml.service.oanda;

public enum Direction {
    LONG, SHORT;

    /**
     * The quantity requested to be filled by the Market Order.
     * A positive number of units results in a long Order, and a negative number of units results in a short Order.
     */

    public double units(final double unit) {
        double abs = Math.abs(unit);
        return this.equals(LONG) ? abs : -abs;
    }
}
