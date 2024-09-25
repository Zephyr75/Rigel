package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 *  Modèle utilisé pour la construction de la Lune
 */
public enum MoonModel implements CelestialObjectModel<Moon>{
    MOON();

    private final static double P0 = Angle.ofDeg(130.143076);
    private final static double N0 = Angle.ofDeg(291.682547);
    private final static double theta0 = Angle.ofDeg(0.5181);
    private final static double l0= Angle.ofDeg(91.929336);
    private final static double i= Angle.ofDeg(5.145396), e= 0.0549, eSquared = e*e;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Creates the moon from given parameters
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return lune avec la conversion associée, sa taille angulaire et sa phase
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double cosI = Math.cos(i);
        double sinI = Math.sin(i);
        double l = Angle.ofDeg(13.1763966) * daysSinceJ2010 + l0;
        double Mm = l - Angle.ofDeg(0.1114041) * daysSinceJ2010 - P0;
        Sun soleil = SunModel.SUN.at(daysSinceJ2010 , eclipticToEquatorialConversion);
        double Mo = soleil.meanAnomaly();
        double sinMo = Math.sin(Mo);
        double lambdaO = soleil.eclipticPos().lon();
        double Ev = Angle.ofDeg(1.2739) * Math.sin(2*(l - lambdaO)- Mm);
        double Ae = Angle.ofDeg(0.1858) * sinMo;
        double A3 = Angle.ofDeg(0.37) * sinMo;
        double MmPrim = Mm + Ev - Ae - A3;
        double Ec = Angle.ofDeg(6.2886) * Math.sin(MmPrim);
        double A4 = Angle.ofDeg(0.214) * Math.sin(2 * MmPrim);
        double lPrim = l + Ev + Ec - Ae + A4;
        double V = Angle.ofDeg(0.6583) * Math.sin(2 * (lPrim - lambdaO));
        double lDoublePrim = lPrim + V;
        double N = N0 - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double NPrim = N - Angle.ofDeg(0.16) * Math.sin(Mo);
        double sinlN = Math.sin(lDoublePrim - NPrim);
        double nominateur = sinlN * cosI;
        double denominateur = Math.cos(lDoublePrim - NPrim);
        double lambdaM = Angle.normalizePositive(Math.atan2(nominateur , denominateur) + NPrim);
        double betaM = Math.asin(sinlN * sinI);
        double p = (1 - eSquared) / (1 + e * Math.cos(MmPrim + Ec));
        double angularSize = theta0 / p;
        double F = (1 - Math.cos(lDoublePrim - lambdaO)) / 2;

        return new Moon(eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambdaM, betaM)), (float) angularSize, 0.f, (float)F );
    }
}