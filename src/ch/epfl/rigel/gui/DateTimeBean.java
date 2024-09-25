package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Temps écoulé sous la forme d'une propriété d'object
 */
public final class DateTimeBean {
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(null);
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);
    private ObjectProperty<ZoneId> zoneId = new SimpleObjectProperty<>(null);


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return la propriété de la date
     */
    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return la date de la propriété
     */
    public LocalDate getDate(){
        return date.get();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Remplace la date par une nouvelle
     * @param date : la nouvelle date
     */
    public void setDate(LocalDate date){
        this.date.set(date);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return la propriété du moment
     */
    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return le moment de la propriété
     */
    public LocalTime getTime(){
        return time.get();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Remplace le moment par un nouveau
     * @param time : le nouveau moment
     */
    public void setTime(LocalTime time){
        this.time.set(time);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return la propriété de la zone géographique
     */
    public ObjectProperty<ZoneId> zoneIdProperty(){
        return zoneId;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return la zone géographique de la propriété
     */
    public ZoneId getZoneId(){
        return zoneId.get();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Remplace la zone géographique par un nouveau
     * @param zoneId : la nouvelle zone géographique
     */
    public void setZoneId(ZoneId zoneId){
        this.zoneId.set(zoneId);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return le moment sous forme de ZonedDateTime
     */
    public ZonedDateTime getZonedDateTime(){
        return ZonedDateTime.of(getDate(), getTime(), getZoneId());
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Remplace le moment par un nouveau sous forme de ZonedDateTime
     * @param moment : le nouveau moment sous forme de ZonedDateTime
     */
    public void setZonedDateTime(ZonedDateTime moment){
        date.set(moment.toLocalDate());
        time.set(moment.toLocalTime());
        zoneId.set(moment.getZone());
    }
}
