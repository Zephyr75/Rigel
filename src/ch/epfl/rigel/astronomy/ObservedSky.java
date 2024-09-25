package ch.epfl.rigel.astronomy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

/**
 *  Génère le ciel observable
 */
public class ObservedSky {
    private double daysSinceJ2010;
    private EclipticToEquatorialConversion eclipToEqua;
    private EquatorialToHorizontalConversion equaToHoriz;
    private StereographicProjection proj;
    private Sun sun;
    private Moon moon;
    private List<Planet> planets = new ArrayList<Planet>();
    private List<Star> stars = new ArrayList<Star>();
    private StarCatalogue catalogue;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur pour le ciel observable
     * @param time : le temps d'observation
     * @param coords : les coordonnées du point d'observation
     * @param proj : la projection stéréographique au point d'observation
     * @param catalogue : le catalogue d'étoiles
     */
    public ObservedSky(ZonedDateTime time, GeographicCoordinates coords, StereographicProjection proj, StarCatalogue catalogue) {
        daysSinceJ2010 = Epoch.J2010.daysUntil(time);
        eclipToEqua = new EclipticToEquatorialConversion(time);
        equaToHoriz = new EquatorialToHorizontalConversion(time, coords);
        this.proj = proj;
        sun = SunModel.SUN.at(daysSinceJ2010, eclipToEqua);
        moon = MoonModel.MOON.at(daysSinceJ2010, eclipToEqua);
        for (int i = 0; i < PlanetModel.ALL.size(); i++) {
            if (PlanetModel.ALL.get(i) != PlanetModel.EARTH) {
                planets.add(PlanetModel.ALL.get(i).at(daysSinceJ2010, eclipToEqua));
            }
        }
        this.stars = catalogue.stars();
        this.catalogue = catalogue;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return soleil
     */
    public Sun sun() {
        return sun;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return coordonnées cartésiennes du soleil
     */
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(proj.apply(equaToHoriz.apply(sun.equatorialPos())).x(),
                proj.apply(equaToHoriz.apply(sun.equatorialPos())).y());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return lune
     */
    public Moon moon() {
        return moon;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return coordonnées cartésiennes de la lune
     */
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(proj.apply(equaToHoriz.apply(moon.equatorialPos())).x(),
                proj.apply(equaToHoriz.apply(moon.equatorialPos())).y());
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return liste des planètes
     */
    public List<Planet> planets(){
        return planets;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return coordonnées cartésiennes des planètes
     */
    public double[] planetPositions() {
        double[] positions = new double[planets.size() * 2];
        int i = 0;
        for (Planet planet : planets) {
            positions[i] = proj.apply(equaToHoriz.apply(planet.equatorialPos())).x();
            i++;
            positions[i] = proj.apply(equaToHoriz.apply(planet.equatorialPos())).y();
            i++;
        }
        return positions;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return liste des étoiles
     */
    public List<Star> stars(){
        return stars;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return coordonnées cartésiennes des étoiles
     */
    public double[] starPositions() {
        double[] positions = new double[stars.size() * 2];
        int i = 0;
        for (Star star : stars) {
            positions[i] = proj.apply(equaToHoriz.apply(star.equatorialPos())).x();
            i++;
            positions[i] = proj.apply(equaToHoriz.apply(star.equatorialPos())).y();
            i++;
        }
        return positions;
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return set des astérismes
     */
    public Set<Asterism> asterisms(){
        return catalogue.asterisms();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param asterism : astérisme étudié
     * @return liste des index de l'astérisme
     */
    public List<Integer> asterismIndices(Asterism asterism){
        return catalogue.asterismIndices(asterism);
    }
    

    private List<CelestialObject> all = new ArrayList<CelestialObject>();
    private double newHigh, currentHigh;
    private CelestialObject currentObject;
    
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param otherCoords coordonnées du point
     * @param distanceMax distance entre la souris et le point
     * @return
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates otherCoords, double distanceMax) {
        if (all.size() == 0) {
            all = initialiseComparison();
        }
        currentHigh = distanceMax;
        for (CelestialObject object : all) {
            if ((newHigh = equaToHoriz.apply(object.equatorialPos()).angularDistanceTo(
                    proj.inverseApply(otherCoords))) <= currentHigh){
                currentHigh = newHigh;
                currentObject = object;
            }
        }
        if (currentHigh < distanceMax) {
            return Optional.of(currentObject);
        }
        return Optional.empty();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return liste contenant tous les éléments du ciel pour initialiser ObjectClosestTo
     */
    private List<CelestialObject> initialiseComparison() {
        List<CelestialObject> objects = new ArrayList<CelestialObject>();
        objects.add(sun);
        objects.add(moon);
        for (Planet planet : planets) {
            objects.add(planet);
        }
        for (Star star : stars) {
            objects.add(star);
        }
        return objects;
    }
    
    
}
