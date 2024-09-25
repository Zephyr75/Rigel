package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Permet de créer des intervalles ouverts à droite
 */
public final class RightOpenInterval extends Interval {

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur prive pour l'intervalle ouvert à droite
     * @param borneInf : borne inférieure de l'intervalle
     * @param borneSup : borne supérieure de l'intervalle
     */
    protected RightOpenInterval(double borneInf, double borneSup) {
        super(borneInf, borneSup);
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Construit un intervalle ouvert à droite
     * @param low : borne inférieure de l'intervalle
     * @param high : borne supérieure de l'intervalle
     * @return l'intervalle correspondant aux bornes données
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Construit un intervalle symetrique de taille donnee centre en 0
     * @param size : la taille de l'intervalle
     * @return l'intervalle symétrique
     */
    public static RightOpenInterval symmetric(double size){
        Preconditions.checkArgument(size > 0);
        double borne = size / 2 ;
        return new RightOpenInterval(-borne , borne);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Calcule le reste de la partie entiere
     * @param x
     * @param y
     * @return reste de la partie entière
     */
    private double floorMod(double x, double y) {
        return x - y * Math.floor(x/y);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Reduit la valeur à l'intervalle
     * @param v : la valeur à remettre dans l'intervalle
     * @return la valeur dans l'intervalle
     */
    public double reduce(double v) {
        if(! this.contains(v)) {
            return this.low() + floorMod(v - this.low(), this.high() - this.low());
        }
        else {
            return v;
        }
    }

    @Override
    public boolean contains(double v) {
        return v >= this.low() && v < this.high();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%.4f;%.4f[", this.low(), this.high());
    }
}