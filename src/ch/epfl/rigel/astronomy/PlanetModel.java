package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Modèle utilisé pour la création des planètes
 */
public enum PlanetModel implements CelestialObjectModel<Planet>{
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);
    private String name;
    private double tp, epsilon, omega, e, a, i, delta, theta, v0;
    public static List<PlanetModel> ALL = List.of(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE);
    private final static double ROTATION_PAR_JOUR = (Angle.TAU / 365.242191);

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for the planetModel
     * @param name
     * @param tp
     * @param epsilon
     * @param omega
     * @param e
     * @param a
     * @param i
     * @param delta
     * @param theta
     * @param v0
     */
    PlanetModel(String name, double tp, double epsilon, double omega, double e, double a, double i, double delta, double theta, double v0){
        this.name = name;
        this.tp = tp;
        this.epsilon = Angle.ofDeg(epsilon);
        this.omega = Angle.ofDeg(omega);
        this.e = e;
        this.a = a;
        this.i = Angle.ofDeg(i);
        this.delta = Angle.ofDeg(delta);
        this.theta = Angle.ofArcsec(theta);
        this.v0 = v0;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Creates a planet from given parameters
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return Planet
     */
    @Override
    public Planet at(double daysSinceJ2010,
                     EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double m2 = computeM2(daysSinceJ2010, tp, epsilon, omega);
        double m2earth = computeM2(daysSinceJ2010, PlanetModel.EARTH.tp, PlanetModel.EARTH.epsilon, PlanetModel.EARTH.omega);
        double v = computeV(m2, e);
        double vearth = computeV(m2earth, PlanetModel.EARTH.e);
        double r = computeR(a, e, v);
        double rearth = computeR(PlanetModel.EARTH.a, PlanetModel.EARTH.e, vearth);
        double l = computeL(v, omega);
        double learth = computeL(vearth, PlanetModel.EARTH.omega);
        double psi = Math.asin(Math.sin(l - delta) * Math.sin(i));
        double rp = r * Math.cos(psi);
        double lp = Math.atan2(Math.sin(l - delta) * Math.cos(i), Math.cos(l - delta)) + delta;
        double lambda;
        if (name.equals(PlanetModel.MERCURY.name) || name.equals(PlanetModel.VENUS.name)) {
            lambda = Math.PI + learth + Math.atan2(rp * Math.sin(learth - lp), rearth - rp * Math.cos(learth - lp));
        }
        else {
            lambda = lp + Math.atan2(rearth * Math.sin(lp - learth), rp - rearth * Math.cos(lp - learth));
        }
        lambda = Angle.normalizePositive(lambda);
        double beta = Math.atan((rp * Math.tan(psi) * Math.sin(lambda - lp))/ (rearth * Math.sin(lp - learth)));
        double rho = Math.sqrt(rearth * rearth + r * r - 2 * rearth * r * Math.cos(l - learth) * Math.cos(psi));
        double f = (1 + Math.cos(lambda - l)) / 2;
        double m = v0 + 5 * Math.log10(r * rho / Math.sqrt(f));

        return new Planet(name, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambda, beta)), (float) (theta/rho), (float) m);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param daysNew
     * @param tpNew
     * @param epNew
     * @param omNew
     * @return
     */
    private double computeM2(double daysNew, double tpNew, double epNew, double omNew) {
        return (ROTATION_PAR_JOUR) * (daysNew / tpNew) + epNew - omNew;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param m2New
     * @param eNew
     * @return
     */
    private double computeV(double m2New, double eNew) {
        return m2New + 2 * eNew * Math.sin(m2New);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param aNew
     * @param eNew
     * @param vNew
     * @return
     */
    private double computeR(double aNew, double eNew, double vNew) {
        return aNew * (1 - eNew * eNew) / (1 + eNew * Math.cos(vNew));
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param vNew
     * @param omNew
     * @return
     */
    private double computeL(double vNew, double omNew) {
        return vNew + omNew;
    }

}