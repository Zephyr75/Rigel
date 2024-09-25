package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 *  Représente des coordonnées sphériques
 */
abstract class SphericalCoordinates {
    final private double longitude;
    final private double latitude;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param Longitude
     * @param Latitude
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude
     */
    double lon() {
        return longitude;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Longitude en degres
     */
    double lonDeg() {
        return Angle.toDeg(lon());
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude
     */
    double lat() {
        return latitude;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return Latitude en degres
     */
    double latDeg() {
        return Angle.toDeg(lat());
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     *  Redéfinit hashCode pour envoyer une UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     *  Effectue une comparaison entre deux éléments
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
