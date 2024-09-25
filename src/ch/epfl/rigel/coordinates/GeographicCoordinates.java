package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;


/**
 *  Représente des coordonnées géographiques
 */
public final class GeographicCoordinates extends SphericalCoordinates {
    private static RightOpenInterval lonInterval = RightOpenInterval.of(-180, 180);
    private static ClosedInterval latInterval = ClosedInterval.of(-90, 90);

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param longitude
     * @param latitude
     */
    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param lonDeg : Longitude en degres
     * @param latDeg : Latitude en degres
     * @return GeographicCoordinates avec coordonnes en degres
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions.checkArgument(isValidLonDeg(lonDeg));
        Preconditions.checkArgument(isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Vrai si longitude valide en degres
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return lonInterval.contains(lonDeg);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Vrai si latitude valide en degres
     */
    public static boolean isValidLatDeg(double latDeg) {
        return latInterval.contains(latDeg);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude en degres
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude en degres
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }
    

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%s.4f°, lat=%s.4f°)", lonDeg(), latDeg());
    }
}
