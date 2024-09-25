package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;


/**
 * Enumération permettant de calculer le temps écoulé depuis des références fixes
 */
public enum Epoch {
    J2000(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneOffset.UTC),
    J2010(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

    private final ZonedDateTime dateTime;
    private final static double MILLIS_TO_DAYS = 24*3600*1000;
    private final static double MILLIS_TO_CENTURIES = 100*365.25*24*3600000;

    /**
     * @author Marin CORNUOT(311984)
     *  constructor for the date
     * @param date
     * @param time
     * @param zone
     */
    private Epoch (LocalDate date, LocalTime time, ZoneOffset zone) {
        dateTime = ZonedDateTime.of(date,time, zone);
    }

    /**
     * @author Marin CORNUOT(311984)
     * retourne le nombre de jours entre this et when
     * @param when
     * @return
     */
    public double daysUntil(ZonedDateTime when) {
        double millisec = dateTime.until(when, ChronoUnit.MILLIS);
        return millisec /MILLIS_TO_DAYS;
    }

    /**
     * @author Marin CORNUOT(311984)
     * retourne le nombre de siècles juliens entre this et when
     * @param when
     * @return
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        double millisec = dateTime.until(when, ChronoUnit.MILLIS);
        return millisec/MILLIS_TO_CENTURIES;
    }


}