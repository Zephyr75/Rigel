package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Classe générale dont hérite tous les objets célestes
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * constructor for a celestial object
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        Preconditions.checkArgument(angularSize>=0);
        Objects.requireNonNull(equatorialPos);
        Objects.requireNonNull(name);
        this.name = name;
        this.magnitude = magnitude;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return name
     */
    public String name(){
        return name;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return angular size
     */
    public double angularSize(){
        return angularSize;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return magnitude
     */
    public double magnitude(){
        return magnitude;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return equatorial position
     */
    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return name
     */
    public String info(){
        return name;
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return name
     */
    @Override
    public String toString(){
        return name;
    }

}