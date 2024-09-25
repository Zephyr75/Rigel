package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;


/**
 *Class with the properties for the position of the observer with the getter and setter for both the longitude
 * the latitude and the geographic coordinates corresponding to them
 */
public class ObserverLocationBean {
    private DoubleProperty lonDeg = new SimpleDoubleProperty();
    private DoubleProperty latDeg = new SimpleDoubleProperty();
    private ObjectProperty<GeographicCoordinates> coordinates = new SimpleObjectProperty<>();

    /**
     *@author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the longitude property
     * @return lonDeg property
     */
    public DoubleProperty lonDegProperty(){
        return lonDeg;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the longitude value
     * @return value of the longitude in degrees
     */
    public Double getLonDeg(){
        return lonDeg.get();
    }

    /**
     * setter for the longitude in degrees
     * @param lonDeg
     */
    public void setLonDeg(double lonDeg){
        this.lonDeg.set(lonDeg);
        this.coordinates.set(GeographicCoordinates.ofDeg(lonDeg, getLatDeg()));
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the lattitude property
     * @return latDeg property
     */
    public DoubleProperty latDegProperty(){
        return latDeg;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the lattitude in degrees
     * @return lattitude value in degrees
     */
    public Double getLatDeg(){
        return latDeg.get();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * setter for the lattitude
     * @param latDeg: new lattitude in degrees
     */
    public void setLatDeg(double latDeg){
        this.latDeg.set(latDeg);
        this.coordinates.set(GeographicCoordinates.ofDeg(getLonDeg(), latDeg));
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the coordinates property
     * @return coordinates the property of the GeographicCoordinates corresponding to the viewers observer location
     */
    public ObjectProperty<GeographicCoordinates> coordinatesProperty(){
        return coordinates;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the observer GeographicCoordinates
     * @return Geographic coordinates of the observer
     */
    public GeographicCoordinates getCoordinates(){
        return GeographicCoordinates.ofDeg(getLonDeg(),getLatDeg());
    }

    /**
     *@author Marin CORNUOT (311984)
     *@author Antoine ROGER (312065)
     * setter for the coordinates
     * @param  coordinates for the new GeographicCoordinates for the observer's Position
     */
    public void setCoordinates(GeographicCoordinates coordinates){
        lonDeg.set(coordinates.lonDeg());
        latDeg.set(coordinates.latDeg());
        this.coordinates.set(coordinates);
    }
}