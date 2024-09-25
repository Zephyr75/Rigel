package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Interface représentant un objet céleste
 * @param <O> paramètres de l'objet à créer
 */
public interface CelestialObjectModel<O> {
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Creates celestialObject from parameters
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return celestialObject
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}