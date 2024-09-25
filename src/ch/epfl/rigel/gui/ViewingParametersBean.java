package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class coordinating with properties the viewer's field of view as well as the HorizontalPosition for the FOV's center
 */
public class ViewingParametersBean {

    private ObjectProperty<Double> fieldOfViewDeg = new SimpleObjectProperty<>();
    private ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    /**
     *@author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the fied of View Property in degrees
     * @return fiedOfView Property in degrees
     */
    public ObjectProperty<Double> fieldOfViewDegProperty(){
        return fieldOfViewDeg;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the value of the FOV in degrees
     * @return fiedOfView value in degrees
     */
    public double getFieldOfViewDeg(){
        return fieldOfViewDeg.get();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * setter for the fieldOfView value in degrees
     * @param fieldOfViewDeg value for the new field of view
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg){
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * getter for the center Property
     * @return center's property
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return center;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * get for the HorizontalCoordinates value of the FOV's center
     * @return center's HorizontalCoordinates value
     */
    public HorizontalCoordinates getCenter(){
        return center.get();
    }

    /**
     * setter for the center's horizontalCoordinates
     * @param center : new HorizontalCoordinates for the FOV'S center
     */
    public void setCenter(HorizontalCoordinates center){
        this.center.set(center);
    }
}