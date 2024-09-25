package ch.epfl.rigel;
import ch.epfl.rigel.math.Interval;

/**
 * Représente les conditions préalables
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Vérifie que l'argument est correct
     * @param isTrue
     */
    public static void checkArgument(boolean isTrue){
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Vérifie que la valeur appartient à l'intervalle
     * @param interval
     * @param value
     * @return value if value is correct
     */
    public static double checkInInterval(Interval interval, double value){
        if (!interval.contains(value)) {
            throw new IllegalArgumentException(" la valeur entrée en paramètre n'est pas comprise dans l'intervalle");
        }
        return value;
    }
}