package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public class Angle {

    public final static double TAU = 2 * Math.PI;
    private static final double RAD_PER_DEG = TAU / 360.0;
    private static final double RAD_PER_ARC = TAU / (360.0 * 3600);
    private static final double RAD_PER_HR = TAU / 24;
    private static final double SEC_PER_MIN = 60;
    private static final RightOpenInterval INTERVAL_MIN_SEC = RightOpenInterval.of(0,60);

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param rad : Angle en radians
     * @return Angle entre 0 et 2 * pi
     */
    public static double normalizePositive(double rad) {
        return RightOpenInterval.of(0, TAU).reduce(rad);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param sec : Angle en secondes d'arc
     * @return Angle en radians
     */
    public static double ofArcsec(double sec) {
        return sec * RAD_PER_ARC;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param deg : Angle en degres
     * @param min : Angle en minutes d'arc
     * @param sec : Angle en secondes d'arc
     * @return Angle en radians
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(INTERVAL_MIN_SEC.contains(min));
        Preconditions.checkArgument(INTERVAL_MIN_SEC.contains(sec));
        Preconditions.checkArgument(deg >=0 );
        return deg * RAD_PER_DEG + min * RAD_PER_ARC * SEC_PER_MIN  + sec * RAD_PER_ARC;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param deg : Angle en degres
     * @return Angle en radians
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param rad : Angle en radians
     * @return Angle en degres
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param hr : Angle en heures
     * @return Angle en radians
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HR;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param rad : Angle en radians
     * @return Angle en heures
     */
    public static double toHr(double rad) {
        return rad / RAD_PER_HR;
    }
}