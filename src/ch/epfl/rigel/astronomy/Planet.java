package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public final class Planet extends CelestialObject {

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * constructor for a planet
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}