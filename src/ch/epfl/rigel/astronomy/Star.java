package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

public final class Star extends CelestialObject{

    private int hipparcosId;
    private float colorIndex;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for the star
     * @param hipparcosId
     * @param name
     * @param equatorialPos
     * @param magnitude
     * @param colorIndex
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        ClosedInterval colorIndexInterval = ClosedInterval.of(-0.5, 5.5);
        Preconditions.checkArgument(colorIndexInterval.contains(colorIndex));
        Preconditions.checkArgument(hipparcosId >= 0);
        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
        }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return hipparcosId of the star
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return colorTemperature of the star
     */
    public int colorTemperature(){
        return (int) (4600*(1/(0.92*colorIndex+1.7)+1/(0.92*colorIndex+0.62)));
    }
}