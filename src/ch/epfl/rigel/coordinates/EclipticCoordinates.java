package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Représente des coordonnées écliptiques
 */
public final class EclipticCoordinates extends SphericalCoordinates{

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param longitude
     * @param latitude
     */
    private EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param lonDeg : Longitude en degres
     * @param latDeg : Latitude en degres
     * @return  EclipticCoordinates si coordonnees correctes
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), Angle.toDeg(lon));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), Angle.toDeg(lat));
        return new EclipticCoordinates(lon, lat);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude
     */
    public double lon() {
        return super.lon(); 
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude en degres
     */
    public double lonDeg() {
        return Angle.toDeg(lon());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude
     */
    public double lat() {
        return super.lat();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude en degres
     */
    public double latDeg() {
        return Angle.toDeg(lat());
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }
}
