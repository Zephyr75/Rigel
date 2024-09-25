package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Conversion de coordonnées équatoriales vers des coordonnées horizontales
 */
public class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{
    private final double local;
    private final double cosWhere;
    private final double sinWhere;

    /**
     * @author Antoine ROGER(312065)
     * construit un changement de systèmes de coordonnées entre les coordonnées équatoriales et les coordonnées horizontales pour le couple date/heure when et le lieu where.
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        cosWhere = Math.cos(where.lat());
        sinWhere = Math.sin(where.lat());
        local = SiderealTime.local(when, where);
    }

    /**
     * @author Antoine ROGER(312065)
     * Retourne les coordonnées horizontales correspondant aux coordonnées équatoriales t.
     * @param
     * @return
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates t) {
        double hourAngle = local - t.ra();
        double cosDec = Math.cos(t.dec());
        double sinDec = Math.sin(t.dec());
        double height = Math.asin(sinDec * sinWhere + cosDec * cosWhere * Math.cos(hourAngle));
        double azimut = Angle.normalizePositive(Math.atan2(- cosDec * cosWhere * Math.sin(hourAngle), sinDec - sinWhere * Math.sin(height)));

        return HorizontalCoordinates.of(azimut, height);
    }

    /**
     * lance une UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * lance une UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}