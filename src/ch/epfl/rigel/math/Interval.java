package ch.epfl.rigel.math;

public abstract class Interval {

    private final double low;
    private final double high;
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for interval
     * @param low
     * @param high
     */
    protected Interval(double low, double high) {
        this.low = low;
        this.high = high;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Borne inferieure
     */
    public double low() {
        return low;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Borne superieure
     */
    public double high() {
        return high;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Taille de l'intervalle
     */
    public double size() {
        return high-low;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @param v : Nombre a tester
     * @return True si le nombre appartient à l'intervalle
     */
    public abstract boolean contains(double v);
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}