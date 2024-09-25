package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;


import java.util.Locale;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Object célestiel de la Lune
 */
public final class Moon extends CelestialObject {
    private final static ClosedInterval interval = ClosedInterval.of(0,1);

    private final float phase;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * constructor for a moon
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     * @param phase
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super ("Lune", equatorialPos, angularSize, magnitude);
        Preconditions.checkArgument(interval.contains(phase));
        this.phase = phase;
    }
    
    public float phase() {
        return phase;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return phase
     */
    @Override
    public String info(){
        return String.format(Locale.ROOT, "%s (%.1f%%)",super.info(), phase*100);
    }

}