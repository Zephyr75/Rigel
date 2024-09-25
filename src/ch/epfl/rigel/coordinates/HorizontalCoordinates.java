package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 *  Représente des coordonnées horizontales
 */
public final class HorizontalCoordinates extends SphericalCoordinates {
    private static int low = 0;
    
    private static final RightOpenInterval intervalNE = RightOpenInterval.of(22.5, 67.5),
                      intervalE = RightOpenInterval.of(67.5, 112.5),
                      intervalSE = RightOpenInterval.of(112.5, 157.5),
                      intervalS = RightOpenInterval.of(157.5, 202.5),
                      intervalSO = RightOpenInterval.of(202.5, 247.5),
                      intervalO = RightOpenInterval.of(247.5, 292.5),
                      intervalNO = RightOpenInterval.of(292.5, 337.5);
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param longitude
     * @param latitude
     */
    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param lonDeg : Longitude en degres
     * @param latDeg : Latitude en degres
     * @return HorizontalCoordinates si coordonnees correctes
     */
    public static HorizontalCoordinates of(double az, double alt) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), Angle.toDeg(az));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), Angle.toDeg(alt));
        return new HorizontalCoordinates(az, alt);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param lonDeg : Longitude en degres
     * @param latDeg : Latitude en degres
     * @return HorizontalCoordinates avec des coordonnees en degres
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), azDeg);
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Azimut
     */
    public double az() {
        return lon();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Azimut en degres
     */
    public double azDeg() {
        return Angle.toDeg(az());
    }
    
    /**
     * Multiplication des bornes inférieures par 10 pour permettre la comparaison avec un switch (qui n'accepte que des entiers)
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param n : Lettre N
     * @param e : Lettre E
     * @param s : Lettre S
     * @param w : Lettre W
     * @return Nom de l'octant correspondant
     */
    public String azOctantName(String n, String e, String s, String w) {
        RightOpenInterval[] intervals = new RightOpenInterval[] {intervalNE, intervalE, intervalSE, intervalS, intervalSO,
                                                                 intervalO, intervalNO};
        low = 0;
        for (RightOpenInterval interval : intervals) {
            if (interval.contains(azDeg())) {
                low = (int) (interval.low() * 10);
            }    
        }
        switch(low) {
            case 225: return n + e;
            case 675: return e;
            case 1125: return s + e;
            case 1575: return s;
            case 2025: return s + w;
            case 2475: return w;
            case 2925: return n + w;
            default: return n;
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Altitude
     */
    public double alt() {
        return lat();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Altitude en degres
     */
    public double altDeg() {
        return Angle.toDeg(alt());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param that : autres coordonnees
     * @return Distance angulaire avec les autres coordonnees
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(alt()) * Math.sin(that.alt()) + Math.cos(alt()) * Math.cos(that.alt()) * Math.cos(az() - that.az()));
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}
