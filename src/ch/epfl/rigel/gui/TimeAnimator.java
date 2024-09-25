package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Animateur pour l'accélérateur du temps
 */
public class TimeAnimator extends AnimationTimer  {
    private ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private SimpleBooleanProperty running = new SimpleBooleanProperty();
    private long elapsedTime = -1;
    public DateTimeBean dateTimeB;
    private ZonedDateTime initialTime;
    private boolean forward = true;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur pour l'animateur
     * @param dateTimeB
     */
    public TimeAnimator(DateTimeBean dateTimeB) {
        this.dateTimeB = dateTimeB;
        this.initialTime = dateTimeB.getZonedDateTime();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     *  Fait évoluer le temps quand l'animateur est lancé
     */
    @Override
    public void handle(long now) {
        if (elapsedTime == -1) {
            elapsedTime = now;
            this.initialTime = dateTimeB.getZonedDateTime();
        }
        if (forward) {
            dateTimeB.setZonedDateTime(accelerator.get().adjust(initialTime, now - elapsedTime));
        }
        else {
            dateTimeB.setZonedDateTime(accelerator.get().adjust(initialTime, elapsedTime - now));
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     *  Lance l'animateur
     */
    @Override
    public void start() {
        elapsedTime = -1;
       running.set(true);
        super.start();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     *  Pause l'animateur
     */
    @Override
    public void stop() {
      running.set(false);
        super.stop();
    }

    /*
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Remplace l'accélérateur par un nouveau
     * @param accelerator : le nouvel accélérateur
     */
    public void setAccelerator(TimeAccelerator accelerator){
        this.accelerator.set(accelerator);
    }

    public SimpleBooleanProperty running() {
        return running;
    }



    public void setDirection(boolean forward) {
        this.forward = forward;
    }
}