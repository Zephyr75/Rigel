package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Représente le temps sidéral
 */
public final class SiderealTime {

    private final static Polynomial polynomes0 = Polynomial.of(0.000025862,2400.051336,6.697374558);
    private final static Polynomial polynomes1 = Polynomial.of(1.002737909,0);
    private final static double MILLIS_PER_HOUR = 3600000.0;

    private SiderealTime(){}

    /**
     *  @author Marin CORNUOT(311984)
     *  Retourne le temps sideral de Greenwich
     * @param when
     * @return
     */
    public static double greenwich(ZonedDateTime when){
        ZonedDateTime whenWithZoneSameInstant = when.withZoneSameInstant(ZoneOffset.UTC);
        double T = Epoch.J2000.julianCenturiesUntil(whenWithZoneSameInstant.truncatedTo(ChronoUnit.DAYS));
        double s0 = polynomes0.at(T);
        double t = whenWithZoneSameInstant.truncatedTo(ChronoUnit.DAYS).until((whenWithZoneSameInstant),ChronoUnit.MILLIS)/ MILLIS_PER_HOUR;
        double s1 = polynomes1.at(t);
        return (Angle.normalizePositive(Angle.ofHr(s0+s1)));
    }

    /**
     * @author Marin CORNUOT(311984)
     * Retourne le temps sideral local
     * @param when
     * @param where
     * @return
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }



}