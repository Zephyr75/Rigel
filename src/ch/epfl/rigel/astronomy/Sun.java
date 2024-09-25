package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public final class Sun extends CelestialObject {

    private final float meanAnomaly;
    private final EclipticCoordinates eclipticPos;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * constructor for the sun
     * @param eclipticPos
     * @param equatorialPos
     * @param angularSize
     * @param meanAnomaly
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly){
        super("Soleil",equatorialPos, angularSize, -26.7f );
        Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
        this.eclipticPos = eclipticPos;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return ecliptic position
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return mean anomaly
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }

}