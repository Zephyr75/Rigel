package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Permet de convertir des coordonnées ecliptiques en coordonnées équatoriales
 */
public class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{
    private double  cosε, sinε;
    private final static Polynomial polynome = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));

    /**
     * @author Antoine ROGER(312065)
     * construit un changement de système de coordonnées entre les coordonnées écliptiques et les coordonnées équatoriales pour le couple date/heure when
     * @param when
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double T = Epoch.J2000.julianCenturiesUntil(when);
        double ε = polynome.at(T);

        cosε = Math.cos(ε);
        sinε = Math.sin(ε);
    }

    /**
     * @author Antoine ROGER(312065)
     * retourne les coordonnées équatoriales correspondant aux coordonnées écliptiques
     * @param t
     * @return
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates t) {
        double sin = Math.sin(t.lon());
        double α = Angle.normalizePositive(Math.atan2( sin * cosε - Math.tan(t.lat()) * sinε, Math.cos(t.lon())));
        double β = Math.asin(Math.sin(t.lat()) * cosε + Math.cos(t.lat()) * sinε * sin);
        return EquatorialCoordinates.of(α,  β);
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