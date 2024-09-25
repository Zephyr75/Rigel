package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;
import ch.epfl.rigel.math.Angle;

/**
 *  Représente une projection stéréographique
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>{
    private final HorizontalCoordinates center;
    private final double sinCenter, cosCenter;
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * constructor for the stereographic projection
     * @param center
     */
    public StereographicProjection(HorizontalCoordinates center) {
        this.center = center;
        this.sinCenter = Math.sin(center.alt());
        this.cosCenter = Math.cos(center.alt());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param hor
     * @return coordinates of the center of the projection of the parallel
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        return CartesianCoordinates.of(0, cosCenter / (Math.sin(hor.alt()) + sinCenter));
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param parallel
     * @return radius of the projection of the parallel
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinCenter);
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param rad
     * @return radius of a sphere centered at the projection point
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad / 4);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return cartesian coordinates of the projection of t
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates t) {
        double lambda = t.az() - center.az();
        double cosAlt = Math.cos(t.alt());
        double sinAlt = Math.sin(t.alt());
        double cosLambda = Math.cos(lambda);
        double sinLambda = Math.sin(lambda);
        double d = 1 / (1 + sinAlt * sinCenter + cosAlt * cosCenter * cosLambda);
        return CartesianCoordinates.of(d * cosAlt * sinLambda, 
             d * (sinAlt * cosCenter - cosAlt * sinCenter * cosLambda));
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param xy
     * @return horizontal coordinates of the point which projection is xy
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x(), y = xy.y();
        double ρ = Math.sqrt(x*x + y*y);
        double sinc = (2 * ρ) / (ρ * ρ + 1);
        double cosc = (1 - ρ * ρ) / (ρ * ρ + 1);
        double centerAz = center.az();
        double centerAlt = center.alt();
        if (x == 0 && y == 0) {
            return HorizontalCoordinates.of(Angle.normalizePositive(centerAz), centerAlt);
        }
        return HorizontalCoordinates.of(Angle.normalizePositive(Math.atan2(x * sinc, ρ * cosCenter * cosc - y * sinCenter * sinc) + centerAz),
                Math.asin(cosc * sinCenter + ((y * sinc * cosCenter) / ρ)));
    }
    
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection: " + center.toString());
    }

}
