package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Accélérateur pour le temps s'écoulant
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Mets à jour le temps écoulé et le temps initial
     * @param initialTime
     * @param elapsedTime
     * @return
     */
    ZonedDateTime adjust(ZonedDateTime initialTime, long elapsedTime);

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Calcule une accélération continue
     * @param α : le facteur d'accélération continue
     * @return
     */
    static TimeAccelerator continuous(int α) {
        return (initialTime, elapsedTime) -> (initialTime.plusNanos(α * elapsedTime));
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Calcule une accélération discrète
     * @param v : le facteur d'accélération discrète
     * @param s : la durée du jour utilisé
     * @return
     */
    static TimeAccelerator discrete(int v, Duration s) {
        return (initialTime, elapsedTime) -> (initialTime.plusSeconds((long) (s.toSeconds() * Math.floor(elapsedTime * v / Math.pow(10, 9)))));
    }
}