package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Permet de créer des intervalles fermés
 */
public final class ClosedInterval extends Interval {

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur pour l'intervalle fermé
     * @param low
     * @param high
     */
    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * COnstruit un intervalle ferme avec ses bornes
     * @param low : Borne inferieure
     * @param high : Borne superieure
     * @throws IllegalArgumentException : Si les bornes sont inversees
     * @return Intervalle ferme
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Construit un intervalle symetrique de taille donnee centre en 0
     * @param size : La taille de l'intervalle
     * @return Intervalle ferme
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        double borne = size/2;
        return new ClosedInterval(-borne, borne);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param v : Valeur a tester
     * @return Valeur clippee
     */
    public double clip(double v) {
        if (v < super.low()) {
            return super.low();
        }
        if (v > super.high()) {
            return super.high();
        }
        return v;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f,%f]", low(), high());
    }

    @Override
    public boolean contains(double v) {
        return v <= high() && v >= low();
    }
}