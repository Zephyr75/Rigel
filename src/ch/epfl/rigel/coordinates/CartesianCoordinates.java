package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Représente des coordonnées cartésiennes
 */
public final class CartesianCoordinates {

    private final double abs;
    private final double ord;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param x
     * @param y
     * Constructor for cartesianCooridnates
     */
    private CartesianCoordinates (double x, double y) {
        abs = x;
        ord = y;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param x
     * @param y
     * @return CartesianCoordinates from x and y coordinates
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x,y);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return abscisse
     */
    public double x(){
        return abs;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return ordonnée
     */
    public double y(){
        return ord;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }


    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"(abs=%.4f°, ord=%.4f°)", x(), y());
    }
}