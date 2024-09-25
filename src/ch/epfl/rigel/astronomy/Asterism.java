package ch.epfl.rigel.astronomy;

import java.util.List;

import ch.epfl.rigel.Preconditions;

/**
 * Représente un astérisme: groupe d'étoiles voisines sur la sphère céleste
 */
public final class Asterism {

    private final List<Star> stars;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for asterism
     * @param stars
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars);
}

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return stars in the asterism
     */
    public List<Star> stars(){
        return stars;
    }
}