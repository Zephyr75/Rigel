package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Modèle utilisé pour la construction du soleil
 */
public enum SunModel implements CelestialObjectModel<Sun>{
    SUN();

    private final static double epsilon = Angle.ofDeg(279.557208), omega = Angle.ofDeg(283.112438), e = 0.016705, rho = Angle.ofDeg(0.533128);
    private final static double ROTATION_PAR_JOUR = (Angle.TAU / 365.242191);
    private final static double pow = (1 - Math.pow(e, 2));

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return Sun
     * Permet de créer le Soleil aux bon endroit en fonction des coordonnées et du jour
     */
    @Override
    public Sun at(double daysSinceJ2010,
                  EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double m = daysSinceJ2010 * ROTATION_PAR_JOUR + epsilon - omega;
        double v = m + 2 * e * Math.sin(m);
        double lambda = Angle.normalizePositive(v + omega);
        double phi = 0;
        double theta = rho * (1 + e * Math.cos(v)) / pow;

        return new Sun(EclipticCoordinates.of(lambda, phi), eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambda, phi)), (float) theta, (float) m);
    }
}