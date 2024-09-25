package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;


/**
 *@author Marin CORNUOT (311984)
 *classe gérant les différents pays et leurs coordonnées
 */
public final class Country {

    private final double longitude;
    private final double latitude;
    private final String name;

    /**
     * @author Marin CORNUOT (311984)
     * constructeur pour les pays
     * @param name : nom du pays
     * @param lon : sa longitude (de son Centroid)
     * @param lat : sa latitude
     */
    public Country(String name, double lon, double lat) {
        Preconditions.checkArgument(GeographicCoordinates.isValidLonDeg(lon));
        Preconditions.checkArgument(GeographicCoordinates.isValidLatDeg(lat));

        longitude = (Math.round(lon * 100.0)/100.0);
        latitude = (Math.round(lat * 100.0)/100.0);
        this.name = name;
    }

    // getter pour le nom d'une planète
    public String getName() {return name;}

    // getter pour la longitude(double) d'un pays
    public double getLongitude() {
        return longitude;
    }

    //getter pour la lattitude (double) d'une planète
    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return name;
    }


}