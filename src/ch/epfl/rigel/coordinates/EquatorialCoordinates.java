package ch.epfl.rigel.coordinates;

import java.util.Locale;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Représente des coordonnées équatoriales
 */
public final class EquatorialCoordinates extends SphericalCoordinates{

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param longitude
     * @param latitude
     */
    private EquatorialCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param lonDeg : Longitude en degres
     * @param latDeg : Latitude en degres
     * @return  EquatorialCoordinates if coordinates are coorect
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), Angle.toDeg(ra));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), Angle.toDeg(dec));
        return new EquatorialCoordinates(ra, dec);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Ascension droite
     */
    public double ra() {
        return lon();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Asscension droite en degres
     */
    public double raDeg() {
        return Angle.toDeg(ra());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Ascension droite en heures
     */
    public double raHr() {
        return Angle.toHr(ra());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Declinaison
     */
    public double dec() {
        return lat();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Declinaison en degres
     */
    public double decDeg() {
        return Angle.toDeg(lat());
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%s.4fh, dec=%s.4f°)", raHr(), decDeg());
    }
}
